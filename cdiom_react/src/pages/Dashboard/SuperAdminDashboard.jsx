import { Card, Row, Col, Statistic, Typography } from 'antd';
import { UserOutlined, SettingOutlined, FileTextOutlined, ClockCircleOutlined } from '@ant-design/icons';

const { Title } = Typography;

/**
 * 系统管理员首页
 */
const SuperAdminDashboard = () => {
  return (
    <div>
      <Title level={2}>系统管理</Title>

      <Row gutter={[16, 16]} style={{ marginTop: 24 }}>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="在线用户"
              value={0}
              prefix={<UserOutlined />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="近24小时操作量"
              value={0}
              prefix={<FileTextOutlined />}
              valueStyle={{ color: '#52c41a' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="系统配置项"
              value={0}
              prefix={<SettingOutlined />}
              valueStyle={{ color: '#722ed1' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="待处理任务"
              value={0}
              prefix={<ClockCircleOutlined />}
              valueStyle={{ color: '#faad14' }}
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]} style={{ marginTop: 24 }}>
        <Col xs={24} lg={12}>
          <Card title="角色分布统计" style={{ height: '100%' }}>
            <p>角色分布图表（待实现）</p>
          </Card>
        </Col>
        <Col xs={24} lg={12}>
          <Card title="各模块操作频次统计" style={{ height: '100%' }}>
            <p>操作频次图表（待实现）</p>
          </Card>
        </Col>
      </Row>

      <Card title="合规配置面板" style={{ marginTop: 24 }}>
        <Row gutter={[16, 16]}>
          <Col xs={24} sm={12}>
            <p><strong>效期阈值：</strong>180天</p>
          </Col>
          <Col xs={24} sm={12}>
            <p><strong>日志保留期：</strong>5年</p>
          </Col>
        </Row>
      </Card>
    </div>
  );
};

export default SuperAdminDashboard;



