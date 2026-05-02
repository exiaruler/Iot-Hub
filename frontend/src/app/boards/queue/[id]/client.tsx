'use client'

import BackButton from "@/app/next-components/buttons/BackButton";
import Content, { ObjectArray } from "@/app/next-components/layout/Content"
import TableComponent from "@/app/next-components/TableComponent"
import TableComponentColumn from "@/components/Table/TableComponentColumn";
import { Col, Row } from "react-bootstrap"

interface Props{
    queue:Record<string, any>[];
    boardId:string;
}
export default function Client(props:Props){
    const convertTime=(dateTime:string)=>{
        return new Date(dateTime).toLocaleTimeString();
    }
    const convertDate=(dateTime:string)=>{
        return new Date(dateTime).toLocaleDateString();
    }
    return(
        <Content>
        <Row>
        <Col md={2} xs={2}>
        <BackButton url={'/boards/'+props.boardId} />
        </Col>
        <Col md={10} xs={14}>
        <TableComponent results={props.queue} idKey={"id"}>
        <TableComponentColumn key={"boardTaskId"} columnName={"Task ID"}/>
        <TableComponentColumn key={"taskName"} columnName={"Task"}/>
        <TableComponentColumn key={"inBoardQueue"} columnName={"In Queue"}/>
        <TableComponentColumn key={"systemQueue"} columnName={"System Task"}/>
        <TableComponentColumn key={"createdDate"} columnName={"Created Date"} functionDisplay={convertDate}/>
        <TableComponentColumn key={"createdDate"} columnName={"Created Time"} functionDisplay={convertTime}/>
        </TableComponent>
        </Col>
        </Row>
        </Content>
    )
}