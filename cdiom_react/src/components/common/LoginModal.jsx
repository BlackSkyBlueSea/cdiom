import { useState } from 'react';
import { Modal, Form, Input, Button, message } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { login } from '@/api/auth';
import { setToken, setUserInfo } from '@/utils/auth';

/**
 * 登录弹窗组件
 */
const LoginModal = ({ open, onClose, onSuccess }) => {
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  const handleSubmit = async (values) => {
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
        form.resetFields();
        onClose();
        // 触发登录成功回调，刷新页面或跳转
        if (onSuccess) {
          onSuccess(response.data);
        } else {
          // 刷新页面以加载新的用户信息
          window.location.reload();
        }
      } else {
        message.error(response.message || '登录失败');
      }
    } catch (error) {
      message.error(error.message || '登录失败，请稍后重试');
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    form.resetFields();
    onClose();
  };

  return (
    <Modal
      title="登录"
      open={open}
      onCancel={handleCancel}
      footer={null}
      width={400}
      destroyOnClose
    >
      <Form
        form={form}
        name="login"
        onFinish={handleSubmit}
        autoComplete="off"
        size="large"
        layout="vertical"
      >
        <Form.Item
          name="usernameOrPhone"
          label="用户名或手机号"
          rules={[
            { required: true, message: '请输入用户名或手机号' },
          ]}
        >
          <Input
            prefix={<UserOutlined />}
            placeholder="请输入用户名或手机号"
          />
        </Form.Item>

        <Form.Item
          name="password"
          label="密码"
          rules={[
            { required: true, message: '请输入密码' },
          ]}
        >
          <Input.Password
            prefix={<LockOutlined />}
            placeholder="请输入密码"
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
    </Modal>
  );
};

export default LoginModal;



