import { Card, Row, Col, Statistic, Typography } from 'antd';
import { ShoppingOutlined, CheckCircleOutlined, WarningOutlined, LineChartOutlined } from '@ant-design/icons';

const { Title } = Typography;

/**
 * 采购专员首页
 */
const PurchaserDashboard = () => {
  return (
    <div>
      <Title level={2}>采购管理</Title>

      <Row gutter={[16, 16]} style={{ marginTop: 24 }}>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="待确认订单"
              value={0}
              prefix={<ShoppingOutlined />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="待发货订单"
              value={0}
              prefix={<CheckCircleOutlined />}
              valueStyle={{ color: '#52c41a' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="库存预警"
              value={0}
              prefix={<WarningOutlined />}
              valueStyle={{ color: '#faad14' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="近30天采购量"
              value={0}
              prefix={<LineChartOutlined />}
              valueStyle={{ color: '#722ed1' }}
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]} style={{ marginTop: 24 }}>
        <Col xs={24} lg={12}>
          <Card title="采购订单进度统计" style={{ height: '100%' }}>
            <p>待确认：0</p>
            <p>待发货：0</p>
            <p>已发货：0</p>
            <p>已入库：0</p>
          </Card>
        </Col>
        <Col xs={24} lg={12}>
          <Card title="需采购药品清单" style={{ height: '100%' }}>
            <p>库存低于预警线的药品列表（待实现）</p>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default PurchaserDashboard;



