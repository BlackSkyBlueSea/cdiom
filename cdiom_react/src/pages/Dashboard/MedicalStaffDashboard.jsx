import { Card, Row, Col, Statistic, Typography } from 'antd';
import { MedicineBoxOutlined, CheckCircleOutlined, ClockCircleOutlined, HeartOutlined } from '@ant-design/icons';

const { Title } = Typography;

/**
 * 医护人员首页
 */
const MedicalStaffDashboard = () => {
  return (
    <div>
      <Title level={2}>药品申领</Title>

      <Row gutter={[16, 16]} style={{ marginTop: 24 }}>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="待审批申领单"
              value={0}
              prefix={<ClockCircleOutlined />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="已通过申领单"
              value={0}
              prefix={<CheckCircleOutlined />}
              valueStyle={{ color: '#52c41a' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="近30天申领统计"
              value={0}
              prefix={<MedicineBoxOutlined />}
              valueStyle={{ color: '#722ed1' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="常用药品"
              value={0}
              prefix={<HeartOutlined />}
              valueStyle={{ color: '#faad14' }}
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]} style={{ marginTop: 24 }}>
        <Col xs={24} lg={12}>
          <Card title="药品申领快捷入口" style={{ height: '100%' }}>
            <p>快速申领药品功能（待实现）</p>
          </Card>
        </Col>
        <Col xs={24} lg={12}>
          <Card title="常用药品收藏栏" style={{ height: '100%' }}>
            <p>常用药品列表（待实现）</p>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default MedicalStaffDashboard;



