import { Card, Row, Col, Statistic, Typography, Tag } from 'antd';
import { WarningOutlined, CheckCircleOutlined, MedicineBoxOutlined, ShoppingCartOutlined } from '@ant-design/icons';

const { Title } = Typography;

/**
 * 仓库管理员首页
 */
const WarehouseAdminDashboard = () => {
  return (
    <div>
      <Title level={2}>仓库管理</Title>

      <Row gutter={[16, 16]} style={{ marginTop: 24 }}>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="近效期预警"
              value={0}
              prefix={<WarningOutlined />}
              valueStyle={{ color: '#faad14' }}
              suffix={
                <div>
                  <Tag color="yellow">黄色预警: 0</Tag>
                  <Tag color="red">红色预警: 0</Tag>
                </div>
              }
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="待入库订单"
              value={0}
              prefix={<ShoppingCartOutlined />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="待审批出库"
              value={0}
              prefix={<CheckCircleOutlined />}
              valueStyle={{ color: '#52c41a' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="库存总量"
              value={0}
              prefix={<MedicineBoxOutlined />}
              valueStyle={{ color: '#722ed1' }}
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]} style={{ marginTop: 24 }}>
        <Col xs={24} lg={12}>
          <Card title="今日出入库统计" style={{ height: '100%' }}>
            <p>统计图表（待实现）</p>
            <p>今日入库：0</p>
            <p>今日出库：0</p>
          </Card>
        </Col>
        <Col xs={24} lg={12}>
          <Card title="待办任务列表" style={{ height: '100%' }}>
            <p>待入库：0</p>
            <p>待审批：0</p>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default WarehouseAdminDashboard;



