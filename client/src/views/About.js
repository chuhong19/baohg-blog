import Button from 'react-bootstrap/Button';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

const About = () => {
  return (
    <>
        <h1>Hello :)</h1>
        <Row className='mt-5' style={{ marginRight: 0 }}>
          <Col className='text-center'>
            <Button variant='primary' href='https://fb.com/baochuhong' size='lg'>
              Visit my facebook for more information
            </Button>
          </Col>
        </Row>
    </>
  );
};

export default About;
