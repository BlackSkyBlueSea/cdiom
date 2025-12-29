import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Input, Button, Card, message } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { login } from '@/api/auth';
import { setToken, setUserInfo } from '@/utils/auth';
import './index.less';

const Login = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const onFinish = async (values) => {
    setLoading(true);
    try {
      const response = await login(values.usernameOrPhone, values.password);
      if (response.code === 200 && response.data) {
        // 存储Token和用户信息
        setToken(response.data.token);
        setUserInfo({
          userId: response.data.userId,
          username: response.data.username,
          phone: response.data.phone,
          roleName: response.data.roleName,
          roleDesc: response.data.roleDesc,
        });

        message.success('登录成功');
        // 跳转到首页或根据权限跳转
        navigate('/');
      } else {
        message.error(response.message || '登录失败');
      }
    } catch (error) {
      message.error(error.message || '登录失败，请稍后重试');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <Card className="login-card" title="CDIOM 医药管理系统">
        <Form
          name="login"
          onFinish={onFinish}
          autoComplete="off"
          size="large"
        >
          <Form.Item
            name="usernameOrPhone"
            rules={[
              { required: true, message: '请输入用户名或手机号' },
            ]}
          >
            <Input
              prefix={<UserOutlined />}
              placeholder="用户名或手机号"
            />
          </Form.Item>

          <Form.Item
            name="password"
            rules={[
              { required: true, message: '请输入密码' },
            ]}
          >
            <Input.Password
              prefix={<LockOutlined />}
              placeholder="密码"
            />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              block
              loading={loading}
            >
              登录
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};

export default Login;
