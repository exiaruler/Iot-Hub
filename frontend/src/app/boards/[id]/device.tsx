'use client'
import DeleteButton from "@/app/next-components/buttons/DeleteButton";
import { RegularButton } from "@/app/next-components/buttons/RegularButton";
import TableComponent from "@/app/next-components/TableComponent";
import TabComponent from "@/components/Tab/TabComponent";
import TabGroup from "@/components/Tab/TabGroup";
import TableComponentColumn from "@/components/Table/TableComponentColumn";
import { Row, Col } from "react-bootstrap";
import { useEffect, useRef } from "react";
import Content, { ContentRef, ObjectRecord } from "@/app/next-components/layout/Content";
interface Props{
    active:boolean;
    device:ObjectRecord;
    board:ObjectRecord;
    index:number;
    deleteDeviceMethod:CallableFunction;
    deleteFunctionMethodAfter:CallableFunction;
}
export default function Device(props:Props){
    const functionTblRef=useRef<TableComponent>(null);
    const contentRef=useRef<ContentRef>(null);
    const tabRef=useRef<TabGroup>(null);
    const openFunction=()=>{
        let id=0;
        const content=contentRef.current;
        const funcRec=functionTblRef.current?.state.selectRowRec;
        if(funcRec!=null){
            id=funcRec.id;
        }
        content?.router.push('/device/function/'+props.board?.boardId+'/'+props.device?.deviceId+'/'+id);
    }
    const deleteFunctionHandle=async (deviceIndex:number)=>{
        const table=functionTblRef.current;
        const selectedRow=table?.returnRow();
        const content=contentRef.current!;
        if(selectedRow!=null){
            const id=selectedRow.id;
            const request=await content.util.fetchClientQuery('/route/delete-route/'+id,'DELETE');
            if(request.status==200&&props.deleteFunctionMethodAfter){
                props.deleteFunctionMethodAfter(deviceIndex,id);
            }
        }
    }
    const loadTab=()=>{
        const content=contentRef.current;
        const tab=tabRef.current;
        const tabName=content?.getQueryField('tab');
        if(tabName!=null){
            tab?.handleTabSwitch(tabName);

        }
    }
    useEffect(()=>{
        loadTab();
    },[])
    return(
        
        <Content ref={contentRef}>
                <TabGroup ref={tabRef} defaultActiveKey={"overview"}>
                <TabComponent title={"Overview"} eventKey={"overview"}>
                <Row>
                <Col>
                <RegularButton caption={"Update Device"} size={undefined} type={undefined}/>
                <RegularButton caption={"Delete Device"} onClick={()=>props.deleteDeviceMethod(props.index)} size={undefined} type={undefined}/>
                </Col>
                </Row>
                </TabComponent>
                <TabComponent title={"Functions"} eventKey={"functions"}>
                <Row>
                <Col>
                <TableComponent ref={functionTblRef} results={props.device?.routes} idKey={"id"} rowSelect={true} onDoubleClick={openFunction}>
                <TableComponentColumn key={"route"} columnName={"Function"} />
                <TableComponentColumn columnName={"Startup"} key={""} size={5}/>
                <TableComponentColumn key={"modes"} columnName={"Modes"} size={5}/>
                </TableComponent>
                </Col>
                </Row>
                <Row>
                <Col>
                <RegularButton caption={"Add Function"} size={undefined} type={undefined} onClick={openFunction}/>
                <DeleteButton caption={"Delete Function"} size={undefined} type={undefined} onClick={()=>deleteFunctionHandle(props.index)}/>
                </Col>
                </Row>
                </TabComponent>
                </TabGroup>
          
        </Content>   
         
    )
}