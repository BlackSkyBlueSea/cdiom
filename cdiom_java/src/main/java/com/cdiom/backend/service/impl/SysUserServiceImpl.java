package com.cdiom.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdiom.backend.common.LoginResponse;
import com.cdiom.backend.common.UserCreateRequest;
import com.cdiom.backend.common.UserDTO;
import com.cdiom.backend.common.UserUpdateRequest;
import com.cdiom.backend.mapper.SysRoleMapper;
import com.cdiom.backend.mapper.SysUserMapper;
import com.cdiom.backend.model.SysRole;
import com.cdiom.backend.model.SysUser;
import com.cdiom.backend.service.SysUserService;
import com.cdiom.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 系统用户服务实现类
 *
 * @author cdiom
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 最大登录失败次数
    private static final int MAX_LOGIN_FAIL_COUNT = 5;
    // 锁定时间（小时）
    private static final int LOCK_HOURS = 1;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(String usernameOrPhone, String password) {
        // 查询用户
        SysUser user = sysUserMapper.selectByUsernameOrPhone(usernameOrPhone);
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 检查是否被锁定
        if (isUserLocked(user)) {
            throw new RuntimeException("账号已被锁定，请1小时后再试或联系管理员解锁");
        }

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            // 增加登录失败次数
            incrementLoginFailCount(user.getId());
            throw new RuntimeException("用户名或密码错误");
        }

        // 登录成功，重置登录失败次数
        resetLoginFailCount(user.getId());

        // 查询角色信息
        SysRole role = sysRoleMapper.selectById(user.getRoleId());
        if (role == null) {
            throw new RuntimeException("用户角色信息不存在");
        }

        // 生成JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), role.getRoleName());

        // 构建登录响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setPhone(user.getPhone());
        response.setRoleName(role.getRoleName());
        response.setRoleDesc(role.getRoleDesc());

        return response;
    }

    @Override
    public SysUser getUserByUsernameOrPhone(String usernameOrPhone) {
        return sysUserMapper.selectByUsernameOrPhone(usernameOrPhone);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementLoginFailCount(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user != null) {
            int failCount = (user.getLoginFailCount() == null ? 0 : user.getLoginFailCount()) + 1;
            user.setLoginFailCount(failCount);

            // 如果达到最大失败次数，锁定账号
            if (failCount >= MAX_LOGIN_FAIL_COUNT) {
                lockUser(userId);
            } else {
                sysUserMapper.updateById(user);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetLoginFailCount(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user != null) {
            user.setLoginFailCount(0);
            user.setLockTime(null);
            sysUserMapper.updateById(user);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lockUser(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user != null) {
            user.setLoginFailCount(MAX_LOGIN_FAIL_COUNT);
            user.setLockTime(LocalDateTime.now());
            sysUserMapper.updateById(user);
        }
    }

    @Override
    public boolean isUserLocked(SysUser user) {
        if (user.getLockTime() == null) {
            return false;
        }

        // 检查锁定时间是否已过
        LocalDateTime now = LocalDateTime.now();
        long hours = ChronoUnit.HOURS.between(user.getLockTime(), now);
        return hours < LOCK_HOURS;
    }

    @Override
    public Page<UserDTO> getUserList(Integer page, Integer size, String keyword, Long roleId, Integer status) {
        Page<UserDTO> pageParam = new Page<>(page, size);
        return sysUserMapper.selectUserList(pageParam, keyword, roleId, status);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        return sysUserMapper.selectUserById(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO createUser(UserCreateRequest request, Long createBy) {
        // 检查用户名是否已存在
        SysUser existUser = sysUserMapper.selectByUsernameOrPhone(request.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查手机号是否已存在
        existUser = sysUserMapper.selectByUsernameOrPhone(request.getPhone());
        if (existUser != null) {
            throw new RuntimeException("手机号已存在");
        }

        // 检查角色是否存在
        SysRole role = sysRoleMapper.selectById(request.getRoleId());
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }

        // 创建用户
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoleId(request.getRoleId());
        user.setStatus(1); // 默认启用
        user.setCreateBy(createBy);
        user.setLoginFailCount(0);

        sysUserMapper.insert(user);

        // 返回用户信息
        return getUserById(user.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO updateUser(UserUpdateRequest request) {
        SysUser user = sysUserMapper.selectById(request.getId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 更新用户名
        if (StringUtils.hasText(request.getUsername()) && !request.getUsername().equals(user.getUsername())) {
            SysUser existUser = sysUserMapper.selectByUsernameOrPhone(request.getUsername());
            if (existUser != null && !existUser.getId().equals(request.getId())) {
                throw new RuntimeException("用户名已存在");
            }
            user.setUsername(request.getUsername());
        }

        // 更新手机号
        if (StringUtils.hasText(request.getPhone()) && !request.getPhone().equals(user.getPhone())) {
            SysUser existUser = sysUserMapper.selectByUsernameOrPhone(request.getPhone());
            if (existUser != null && !existUser.getId().equals(request.getId())) {
                throw new RuntimeException("手机号已存在");
            }
            user.setPhone(request.getPhone());
        }

        // 更新密码
        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // 更新角色
        if (request.getRoleId() != null) {
            SysRole role = sysRoleMapper.selectById(request.getRoleId());
            if (role == null) {
                throw new RuntimeException("角色不存在");
            }
            user.setRoleId(request.getRoleId());
        }

        // 更新状态
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }

        sysUserMapper.updateById(user);

        return getUserById(user.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(Long userId, Integer status) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setStatus(status);
        sysUserMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlockUser(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setLockTime(null);
        user.setLoginFailCount(0);
        sysUserMapper.updateById(user);
    }
}



