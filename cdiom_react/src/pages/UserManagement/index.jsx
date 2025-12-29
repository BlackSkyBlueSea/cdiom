import { useState, useEffect } from 'react';
import {
  Table,
  Button,
  Input,
  Space,
  Modal,
  Form,
  Select,
  message,
  Tag,
  Popconfirm,
  Card,
} from 'antd';
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  UnlockOutlined,
  SearchOutlined,
  ReloadOutlined,
} from '@ant-design/icons';
import {
  getUserList,
  createUser,
  updateUser,
  updateUserStatus,
  unlockUser,
  getRoleList,
} from '@/api/user';
import dayjs from 'dayjs';

const { Search } = Input;

/**
 * 用户管理页面
 */
const UserManagement = () => {
  const [users, setUsers] = useState([]);
  const [roles, setRoles] = useState([]);
  const [loading, setLoading] = useState(false);
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 10,
    total: 0,
  });
  const [modalVisible, setModalVisible] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [form] = Form.useForm();
  const [searchKeyword, setSearchKeyword] = useState('');
  const [selectedRoleId, setSelectedRoleId] = useState(null);
  const [selectedStatus, setSelectedStatus] = useState(null);

  // 加载角色列表
  useEffect(() => {
    loadRoles();
  }, []);

  // 加载用户列表
  useEffect(() => {
    loadUsers();
  }, [pagination.current, pagination.pageSize, searchKeyword, selectedRoleId, selectedStatus]);

  const loadRoles = async () => {
    try {
      const response = await getRoleList();
      console.log('角色列表响应:', response); // 调试用
      if (response && response.code === 200) {
        const roleData = response.data || [];
        console.log('角色数据:', roleData); // 调试用
        setRoles(roleData);
        if (roleData.length === 0) {
          message.warning('暂无角色数据，请先初始化角色');
        }
      } else {
        message.error('加载角色列表失败: ' + (response?.message || '未知错误'));
      }
    } catch (error) {
      console.error('加载角色列表失败:', error);
      message.error('加载角色列表失败: ' + (error.message || '请检查网络连接'));
    }
  };

  const loadUsers = async () => {
    setLoading(true);
    try {
      const response = await getUserList({
        page: pagination.current,
        size: pagination.pageSize,
        keyword: searchKeyword || undefined,
        roleId: selectedRoleId || undefined,
        status: selectedStatus !== null ? selectedStatus : undefined,
      });

      if (response.code === 200 && response.data) {
        setUsers(response.data.records || []);
        setPagination({
          ...pagination,
          total: response.data.total || 0,
        });
      }
    } catch (error) {
      message.error('加载用户列表失败');
    } finally {
      setLoading(false);
    }
  };

  const handleAdd = () => {
    setEditingUser(null);
    form.resetFields();
    setModalVisible(true);
  };

  const handleEdit = (record) => {
    setEditingUser(record);
    form.setFieldsValue({
      id: record.id,
      username: record.username,
      phone: record.phone,
      roleId: record.roleId,
      status: record.status,
    });
    setModalVisible(true);
  };

  const handleDelete = async (userId) => {
    try {
      const response = await updateUserStatus(userId, 0);
      if (response.code === 200) {
        message.success('禁用用户成功');
        loadUsers();
      }
    } catch (error) {
      message.error('禁用用户失败');
    }
  };

  const handleUnlock = async (userId) => {
    try {
      const response = await unlockUser(userId);
      if (response.code === 200) {
        message.success('解锁用户成功');
        loadUsers();
      }
    } catch (error) {
      message.error('解锁用户失败');
    }
  };

  const handleEnable = async (userId) => {
    try {
      const response = await updateUserStatus(userId, 1);
      if (response.code === 200) {
        message.success('启用用户成功');
        loadUsers();
      }
    } catch (error) {
      message.error('启用用户失败');
    }
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      let response;

      if (editingUser) {
        // 更新用户
        response = await updateUser(values);
      } else {
        // 创建用户
        response = await createUser(values);
      }

      if (response.code === 200) {
        message.success(editingUser ? '更新用户成功' : '创建用户成功');
        setModalVisible(false);
        form.resetFields();
        loadUsers();
      }
    } catch (error) {
      if (error.errorFields) {
        return; // 表单验证错误
      }
      message.error(error.message || '操作失败');
    }
  };

  const handleSearch = (value) => {
    setSearchKeyword(value);
    setPagination({ ...pagination, current: 1 });
  };

  const handleReset = () => {
    setSearchKeyword('');
    setSelectedRoleId(null);
    setSelectedStatus(null);
    setPagination({ ...pagination, current: 1 });
  };

  const columns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 80,
    },
    {
      title: '用户名',
      dataIndex: 'username',
      key: 'username',
    },
    {
      title: '手机号',
      dataIndex: 'phone',
      key: 'phone',
    },
    {
      title: '角色',
      dataIndex: 'roleDesc',
      key: 'roleDesc',
      render: (text, record) => (
        <Tag color="blue">{text || record.roleName}</Tag>
      ),
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status, record) => {
        if (record.isLocked) {
          return <Tag color="red">已锁定</Tag>;
        }
        return status === 1 ? (
          <Tag color="green">正常</Tag>
        ) : (
          <Tag color="default">禁用</Tag>
        );
      },
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
      render: (text) => (text ? dayjs(text).format('YYYY-MM-DD HH:mm:ss') : '-'),
    },
    {
      title: '操作',
      key: 'action',
      width: 250,
      render: (_, record) => (
        <Space size="small">
          <Button
            type="link"
            icon={<EditOutlined />}
            onClick={() => handleEdit(record)}
          >
            编辑
          </Button>
          {record.isLocked ? (
            <Popconfirm
              title="确定要解锁此用户吗？"
              onConfirm={() => handleUnlock(record.id)}
            >
              <Button type="link" icon={<UnlockOutlined />}>
                解锁
              </Button>
            </Popconfirm>
          ) : record.status === 1 ? (
            <Popconfirm
              title="确定要禁用此用户吗？"
              onConfirm={() => handleDelete(record.id)}
            >
              <Button type="link" danger icon={<DeleteOutlined />}>
                禁用
              </Button>
            </Popconfirm>
          ) : (
            <Popconfirm
              title="确定要启用此用户吗？"
              onConfirm={() => handleEnable(record.id)}
            >
              <Button type="link" icon={<UnlockOutlined />}>
                启用
              </Button>
            </Popconfirm>
          )}
        </Space>
      ),
    },
  ];

  return (
    <div>
      <Card>
        <Space style={{ marginBottom: 16, width: '100%', justifyContent: 'space-between' }}>
          <Space>
            <Search
              placeholder="搜索用户名或手机号"
              allowClear
              style={{ width: 250 }}
              onSearch={handleSearch}
              enterButton
            />
            <Select
              placeholder="选择角色"
              allowClear
              style={{ width: 150 }}
              value={selectedRoleId}
              onChange={setSelectedRoleId}
            >
              {roles.map((role) => (
                <Select.Option key={role.id} value={role.id}>
                  {role.roleDesc || role.roleName}
                </Select.Option>
              ))}
            </Select>
            <Select
              placeholder="选择状态"
              allowClear
              style={{ width: 120 }}
              value={selectedStatus}
              onChange={setSelectedStatus}
            >
              <Select.Option value={1}>正常</Select.Option>
              <Select.Option value={0}>禁用</Select.Option>
            </Select>
            <Button icon={<ReloadOutlined />} onClick={loadUsers}>
              刷新
            </Button>
            <Button onClick={handleReset}>重置</Button>
          </Space>
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
            新增用户
          </Button>
        </Space>

        <Table
          columns={columns}
          dataSource={users}
          rowKey="id"
          loading={loading}
          pagination={{
            ...pagination,
            showSizeChanger: true,
            showTotal: (total) => `共 ${total} 条`,
            onChange: (page, pageSize) => {
              setPagination({ ...pagination, current: page, pageSize });
            },
          }}
        />
      </Card>

      <Modal
        title={editingUser ? '编辑用户' : '新增用户'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => {
          setModalVisible(false);
          form.resetFields();
        }}
        width={600}
        destroyOnClose
      >
        <Form
          form={form}
          layout="vertical"
          initialValues={{
            status: 1,
          }}
        >
          {editingUser && (
            <Form.Item name="id" hidden>
              <Input />
            </Form.Item>
          )}
          <Form.Item
            name="username"
            label="用户名"
            rules={[
              { required: true, message: '请输入用户名' },
              { min: 3, message: '用户名至少3个字符' },
              { max: 50, message: '用户名最多50个字符' },
            ]}
          >
            <Input placeholder="请输入用户名" />
          </Form.Item>
          <Form.Item
            name="phone"
            label="手机号"
            rules={[
              { required: true, message: '请输入手机号' },
              { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确' },
            ]}
          >
            <Input placeholder="请输入手机号" />
          </Form.Item>
          {!editingUser && (
            <Form.Item
              name="password"
              label="密码"
              rules={[
                { required: true, message: '请输入密码' },
                { min: 6, message: '密码至少6个字符' },
              ]}
            >
              <Input.Password placeholder="请输入密码" />
            </Form.Item>
          )}
          {editingUser && (
            <Form.Item
              name="password"
              label="密码（留空则不修改）"
              rules={[
                { min: 6, message: '密码至少6个字符' },
              ]}
            >
              <Input.Password placeholder="留空则不修改密码" />
            </Form.Item>
          )}
          <Form.Item
            name="roleId"
            label="角色"
            rules={[{ required: true, message: '请选择角色' }]}
          >
            <Select
              placeholder={roles.length === 0 ? '加载角色中...' : '请选择角色'}
              showSearch
              optionFilterProp="children"
              loading={roles.length === 0}
              notFoundContent={roles.length === 0 ? '正在加载角色列表...' : '暂无角色数据'}
            >
              {roles.map((role) => (
                <Select.Option key={role.id} value={role.id}>
                  {role.roleDesc || role.roleName}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>
          {editingUser && (
            <Form.Item name="status" label="状态">
              <Select>
                <Select.Option value={1}>正常</Select.Option>
                <Select.Option value={0}>禁用</Select.Option>
              </Select>
            </Form.Item>
          )}
        </Form>
      </Modal>
    </div>
  );
};

export default UserManagement;

