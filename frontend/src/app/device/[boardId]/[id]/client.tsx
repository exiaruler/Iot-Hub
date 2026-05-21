'use client'
import Content, { ObjectRecord } from "@/app/next-components/layout/Content"
import Col from "react-bootstrap/esm/Col"
import Row from "react-bootstrap/esm/Row"
interface Props{
    device:ObjectRecord;
    board:ObjectRecord;
}

export default function Client(props:Props){
    return(
        <Content>
        <Row>
        <Col sm={12} md={6} lg={4}>
        </Col>
        <Col sm={12} md={6} lg={4}>
        </Col>
        </Row>
        </Content>
    )
}