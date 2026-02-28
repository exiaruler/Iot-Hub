import React, { useEffect, useRef, useState } from 'react';
import '../App.css';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useSelector} from 'react-redux';
import { getLoginState } from '../redux/slice/loginSlice';
import ContentEditor from '../components/content/ContentEditor';
export default function Home(){
  let login=useSelector(getLoginState);


    return (
        <div className="App">
          <Row>
          <Col md={3} xs={3}>
          </Col>
          <Col  md={6} xs={12}>
          <ContentEditor/> 
          </Col>
          </Row>
        </div>
      );
}