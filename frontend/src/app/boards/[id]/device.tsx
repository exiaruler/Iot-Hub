'use client'
import DeleteButton from "@/app/next-components/buttons/DeleteButton";
import { RegularButton } from "@/app/next-components/buttons/RegularButton";
import TableComponent from "@/app/next-components/TableComponent";
import TabComponent from "@/components/Tab/TabComponent";
import TabGroup from "@/components/Tab/TabGroup";
import TableComponentColumn from "@/components/Table/TableComponentColumn";
import { Row, Col } from "react-bootstrap";
import { useRouter } from 'next/navigation';
import { useRef } from "react";
interface Props{
    active:boolean;
    device:Record<string,any>;
    board:Record<string,any>;
    key:number;
}
export default function Device(props:Props){
    const functionTblRef:any=useRef(null);
    const router=useRouter();

    const openFunction=()=>{
        let id=0;
        const funcRec=functionTblRef.current.state.selectRowRec;
        if(funcRec!=null){
            id=funcRec.id;
        }
        router.push('/device/function/'+props.board.boardId+'/'+props.device.deviceId+'/'+id);
    }
    return(
        <div>
         <TabComponent title={props.device.name} eventKey={props.key} disabled={!props.active}>
                <TabGroup defaultActiveKey={"overview"}>
                <TabComponent title={"Overview"} eventKey={"overview"}>
                <Row>
                <Col>
                <RegularButton caption={"Update Device"} size={undefined} type={undefined}/>
                <RegularButton caption={"Delete Device"} size={undefined} type={undefined}/>
                </Col>
                </Row>
                </TabComponent>
                <TabComponent title={"Functions"} eventKey={"functions"}>
                <Row>
                <Col>
                <TableComponent ref={functionTblRef} results={props.device.routes} idKey={"id"} rowSelect={true} onDoubleClick={openFunction}>
                <TableComponentColumn key={"route"} columnName={"Function"} />
                <TableComponentColumn columnName={"Startup"} key={""} size={5}/>
                <TableComponentColumn key={"modes"} columnName={"Modes"} size={5}/>
                </TableComponent>
                </Col>
                </Row>
                <Row>
                <Col>
                <RegularButton caption={"Add Function"} size={undefined} type={undefined} onClick={openFunction}/>
                <DeleteButton caption={"Delete Function"} size={undefined} type={undefined}/>
                </Col>
                </Row>
                </TabComponent>
                </TabGroup>
            </TabComponent>   
        </div>
    )
}