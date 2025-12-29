import { Card, Row, Col, Statistic, Typography } from 'antd';
import { UserOutlined, ShoppingOutlined, MedicineBoxOutlined, WarningOutlined } from '@ant-design/icons';
import { getUserRole } from '@/utils/auth';
import SuperAdminDashboard from './SuperAdminDashboard';
import WarehouseAdminDashboard from './WarehouseAdminDashboard';
import PurchaserDashboard from './PurchaserDashboard';
import MedicalStaffDashboard from './MedicalStaffDashboard';
import SupplierDashboard from './SupplierDashboard';

const { Title } = Typography;

/**
 * 仪表盘首页
 * 根据角色显示不同的首页内容
 */
const Dashboard = () => {
  const roleName = getUserRole();

  const renderDashboard = () => {
    switch (roleName) {
      case 'SUPER_ADMIN':
        return <SuperAdminDashboard />;
      case 'WAREHOUSE_ADMIN':
        return <WarehouseAdminDashboard />;
      case 'PURCHASER':
        return <PurchaserDashboard />;
      case 'MEDICAL_STAFF':
        return <MedicalStaffDashboard />;
      case 'SUPPLIER':
        return <SupplierDashboard />;
      default:
        return (
          <Card>
            <Title level={4}>欢迎使用 CDIOM 医药管理系统</Title>
            <p>请先登录以访问系统功能</p>
          </Card>
        );
    }
  };

  return <div className="dashboard-container">{renderDashboard()}</div>;
};

export default Dashboard;



