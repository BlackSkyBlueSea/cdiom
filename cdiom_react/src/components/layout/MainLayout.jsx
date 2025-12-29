import { useState } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import { Layout, Menu, Dropdown, Button, Avatar, Space } from 'antd';
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UserOutlined,
  LogoutOutlined,
  HomeOutlined,
} from '@ant-design/icons';
import { getUserInfo, clearAuth, getUserRole } from '@/utils/auth';
import { logout } from '@/api/auth';
import { message } from 'antd';
import './MainLayout.less';

const { Header, Sider, Content } = Layout;

/**
 * 主布局组件
 * 包含头部、侧边栏、主内容区
 */
const MainLayout = () => {
  const navigate = useNavigate();
  const userInfo = getUserInfo();
  const roleName = getUserRole();
  const [collapsed, setCollapsed] = useState(false);

  // 根据角色获取菜单项
  const getMenuItems = () => {
    const baseItems = [
      {
        key: '/app',
        icon: <HomeOutlined />,
        label: '首页',
      },
    ];

    // 根据角色添加不同的菜单项
    switch (roleName) {
      case 'SUPER_ADMIN':
        return [
          ...baseItems,
          {
            key: '/app/users',
            icon: <UserOutlined />,
            label: '用户管理',
          },
          {
            key: '/app/system',
            icon: <UserOutlined />,
            label: '系统配置',
          },
        ];
      case 'WAREHOUSE_ADMIN':
        return [
          ...baseItems,
          {
            key: '/app/drugs',
            icon: <UserOutlined />,
            label: '药品管理',
          },
          {
            key: '/app/inbound',
            icon: <UserOutlined />,
            label: '入库验收',
          },
          {
            key: '/app/outbound',
            icon: <UserOutlined />,
            label: '出库审批',
          },
          {
            key: '/app/inventory',
            icon: <UserOutlined />,
            label: '库存管理',
          },
        ];
      case 'PURCHASER':
        return [
          ...baseItems,
          {
            key: '/app/suppliers',
            icon: <UserOutlined />,
            label: '供应商管理',
          },
          {
            key: '/app/orders',
            icon: <UserOutlined />,
            label: '采购订单',
          },
        ];
      case 'MEDICAL_STAFF':
        return [
          ...baseItems,
          {
            key: '/app/apply',
            icon: <UserOutlined />,
            label: '药品申领',
          },
          {
            key: '/app/apply-history',
            icon: <UserOutlined />,
            label: '申领记录',
          },
        ];
      case 'SUPPLIER':
        return [
          ...baseItems,
          {
            key: '/app/my-orders',
            icon: <UserOutlined />,
            label: '我的订单',
          },
        ];
      default:
        return baseItems;
    }
  };

  const handleMenuClick = ({ key }) => {
    navigate(key);
  };

  const handleLogout = async () => {
    try {
      await logout();
      clearAuth();
      message.success('已退出登录');
      navigate('/');
    } catch (error) {
      clearAuth();
      navigate('/');
    }
  };

  const userMenuItems = [
    {
      key: 'userInfo',
      label: (
        <Space>
          <UserOutlined />
          <span>{userInfo?.username || '用户'}</span>
        </Space>
      ),
      disabled: true,
    },
    {
      type: 'divider',
    },
    {
      key: 'logout',
      label: (
        <Space>
          <LogoutOutlined />
          <span>退出登录</span>
        </Space>
      ),
      onClick: handleLogout,
    },
  ];

  return (
    <Layout className="main-layout">
      <Sider
        trigger={null}
        collapsible
        collapsed={collapsed}
        className="main-sider"
      >
        <div className="logo">
          {collapsed ? (
            <img src="/logo.png" alt="CDIOM" className="logo-icon" />
          ) : (
            <div className="logo-text">
              <img src="/logo.png" alt="CDIOM" className="logo-img" />
              <div className="logo-title">
                <div>CDIOM</div>
                <div style={{ fontSize: '12px', opacity: 0.9 }}>医药管理系统</div>
              </div>
            </div>
          )}
        </div>
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={[window.location.pathname]}
          items={getMenuItems()}
          onClick={handleMenuClick}
        />
      </Sider>
      <Layout>
        <Header className="main-header">
          <div className="header-left">
            <Button
              type="text"
              icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
              onClick={() => setCollapsed(!collapsed)}
              className="trigger"
            />
          </div>
          <div className="header-right">
            <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
              <Space className="user-info" style={{ cursor: 'pointer' }}>
                <Avatar icon={<UserOutlined />} />
                <span>{userInfo?.username || '用户'}</span>
                <span className="role-tag">
                  ({userInfo?.roleDesc || userInfo?.roleName || '未登录'})
                </span>
              </Space>
            </Dropdown>
          </div>
        </Header>
        <Content className="main-content">
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
};

export default MainLayout;

