'use client'

import Row from "react-bootstrap/esm/Row";
import Content from "./next-components/layout/Content";
import ContentEditor from "./next-components/ContentEditor";

export default function Client(){
  return (
  <div>
  <Content>
  <Row>
  <ContentEditor/>
  </Row>
  </Content>
  </div>
  );
}