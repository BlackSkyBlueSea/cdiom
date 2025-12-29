import { Card, Row, Col, Statistic, Typography } from 'antd';
import { ShoppingOutlined, CheckCircleOutlined, TruckOutlined, LineChartOutlined } from '@ant-design/icons';

const { Title } = Typography;

/**
 * 供应商首页
 */
const SupplierDashboard = () => {
  return (
    <div>
      <Title level={2}>订单管理</Title>

      <Row gutter={[16, 16]} style={{ marginTop: 24 }}>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="待处理订单"
              value={0}
              prefix={<ShoppingOutlined />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="已发货订单"
              value={0}
              prefix={<TruckOutlined />}
              valueStyle={{ color: '#52c41a' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="近30天订单统计"
              value={0}
              prefix={<LineChartOutlined />}
              valueStyle={{ color: '#722ed1' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="已完成订单"
              value={0}
              prefix={<CheckCircleOutlined />}
              valueStyle={{ color: '#faad14' }}
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]} style={{ marginTop: 24 }}>
        <Col xs={24}>
          <Card title="已发货订单跟踪列表" style={{ height: '100%' }}>
            <p>订单列表（待实现）</p>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default SupplierDashboard;



