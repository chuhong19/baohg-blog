import React from 'react';
import Card from 'react-bootstrap/Card';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Badge from 'react-bootstrap/Badge';
import { Button } from 'react-bootstrap';
import infoIcon from '../../assets/info.svg';
import MyPostActionButton from './MyPostActionButton';

const MySinglePost = ({
  post: { id, title, content },
  onShowInfoModal,
}) => {

  const handleShowInfoModal = () => {
    onShowInfoModal();
  };

  let body = (
    <Card className='shadow'>
      <Card.Body>
        <Card.Title>
          <Row>
            <Col>
              <p className='product-title'>{title}</p>
              <Badge variant='info'>Public</Badge>
            </Col>
            <Col className='text-right'>
              <MyPostActionButton
                id={id}
              />
            </Col>
            <Col>
              <Button onClick={handleShowInfoModal}>
                <img src={infoIcon} alt="info-post" width='60' height='60' />
              </Button>
            </Col>
          </Row>
        </Card.Title>
        <Card.Text>{content}</Card.Text>
        <Card.Text>ID: {id}</Card.Text>
      </Card.Body>
    </Card>
  );

  return (
    <>
      {body}
    </>
  );
};

export default MySinglePost;
