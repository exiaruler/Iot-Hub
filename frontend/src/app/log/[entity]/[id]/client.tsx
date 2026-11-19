'use client'

import BackButton from "@/app/next-components/buttons/BackButton"
import Content, { ObjectArray, stringDate, stringTime } from "@/app/next-components/layout/Content"
import TableComponent from "@/app/next-components/TableComponent"
import TableComponentColumn from "@/components/Table/TableComponentColumn"
import { Col, Row } from "react-bootstrap"

interface Props {
    logs: ObjectArray;
}

export default function Client({ logs }: Props) {
    const displayTime = (date: string | Date | null) => stringTime(date);
    const displayDate = (date: string | Date | null) => stringDate(date);

    return (
        <Content>
            <Row>
                <Col md={2} >
                <BackButton url={""}/>
                </Col>
                <Col md={10}>
                    <TableComponent results={logs} idKey={"id"}>
                        <TableComponentColumn key={"type"} columnName={"Response Type"} />
                        <TableComponentColumn key={"message"} columnName={"Message"} />
                        <TableComponentColumn key={"createdDate"} columnName={"Time"} functionDisplay={displayTime} />
                        <TableComponentColumn key={"createdDate"} columnName={"Date"} functionDisplay={displayDate} />
                    </TableComponent>
                </Col>
            </Row>
        </Content>
    )
}