'use client'
import TabComponent from "@/components/Tab/TabComponent";
import TabGroup from "@/components/Tab/TabGroup";
import TableComponentColumn from "@/components/Table/TableComponentColumn";
import { Col, Row, Stack } from "react-bootstrap";
import TableComponent from "../next-components/TableComponent";
import { useRef, useState } from "react";
import SelectInput from "../next-components/input/SelectInput";
import SaveButton from "../next-components/buttons/SaveButton";
import { RegularButton } from "../next-components/buttons/RegularButton";
import NewButton from "../next-components/form/NewButton";
import Form from "../next-components/form/Form";
import TextInput from "../next-components/input/TextInput";
import CheckBoxInput from "../next-components/input/CheckBoxInput";
import TimeInput from "../next-components/input/TimeInput";
import Content, { ContentRef, ObjectArray, ObjectRecord } from "../next-components/layout/Content";
import ContentEditor from "@/components/content/ContentEditor";
import ContentInput from "@/components/input/ContentInput";
interface Props{
    form:ObjectRecord;
    records:ObjectArray;
}
export default function Client({form,records}:Props){
    const [modeSelectView,setModeSelectView]=useState(true);
    const [recordList,setRecords]=useState<ObjectArray>(records);
    const [deleteBtn,setDeleteBtn]=useState(true);
    const [selectedRecord,setSelectedRecord]=useState<ObjectRecord>(null);
    const [functions,setFunctions]=useState([]);
    const [modes,setModes]=useState([]);
    const tabRef=useRef<TabGroup>(null);
    const formRef=useRef<Form>(null);
    const tableRef=useRef<TableComponent|null>(null);

    const [routineHidden,setRoutineHidden]=useState({
        startup:false,
        routine:false
    })
    const contentRef=useRef<ContentRef>(null);
    
    const submit=()=>{
        let form=formRef.current;
        const tabGroup=tabRef.current;
        const table=tableRef.current;
        const content=contentRef.current;
        if(form?.statusResponse==200){
            const data=form.submissionResponse;
            const index=table?.getRowIndex()||-1;
            if(index>-1){
                setRecords(content?.updateArrayByIndex(data,index,recordList)||recordList);
            }else {
                setRecords((prev:ObjectArray)=>[...prev.filter((rec:ObjectRecord)=>rec?.id!=data.id),data]);
            }
            form.newRecord();
            setSelectedRecord(null);
            tabGroup?.handleTabSwitch('firmware');
        }
    }
    const backList=()=>{
        tabRef.current?.handleTabSwitch('firmware');
    }
    
    const newRecord=()=>{
        setSelectedRecord(null);
        setRoutineHidden({...routineHidden,routine:false,startup:false});
        const table=tableRef.current;
        table?.clearRowSelect();
    }
    
    const selectRecord=(record:ObjectRecord)=>{
        let table=tableRef.current;
        let form=formRef.current;
        if(!table?.sameRow){
            setRoutineHidden({...routineHidden,routine:false,startup:false});
            setSelectedRecord(record);
            form?.setRecord(record);
        }else
        {
            form?.newRecord();
            setRoutineHidden({...routineHidden,routine:false,startup:false});
            setFunctions([]);
            setModeSelectView(true);
            setModes([]);
            setDeleteBtn(true);
        }
    }
 
    
    return(
        <div>
        <Content ref={contentRef}>
        <Row>
        <Col md={2}></Col>
        <Col md={9}>
        <TabGroup defaultActiveKey={"firmware"} ref={tabRef}>
        <TabComponent title={"Firmware Versions"} eventKey={"firmware"}>
        <TableComponent ref={tableRef} results={recordList} idKey={"id"} rowSelect={true} onClick={selectRecord}>
        <TableComponentColumn key={"version"} columnName={"Version"} />

        <TableComponentColumn key={"latest"} columnName={"Latest"}/>
        </TableComponent> 
        </TabComponent>
        <TabComponent title={"Firmware"} eventKey={"form"}>
        <Row>
        <Col md={10}>
        <Form  post={"/firmware/add-record"} put={"/firmware/update-record/"} recordLayout={form||{}} ref={formRef} onSubmit={submit} idKey={"id"}>
        <Row>
        <Col>
        <TextInput disable={selectedRecord!=null} formRef={formRef} name={"version"} label={"Version"} rows={0}/>
        <CheckBoxInput name={'mandatoryUpdate'} disable={selectedRecord!=null} formRef={formRef} label={"Required Update"} rows={0}/>
        <ContentInput contentHeight="500px" formRef={formRef} name={'notes'} label={"Firmware Notes"} rows={0}/>
        </Col>
        <Col>
        <TextInput formRef={formRef} name={'mainFile'} label={"Main Program File Link"} rows={0}/>
        <TextInput formRef={formRef} name={'bootloaderFile'} label={"Bootloader File Link"} rows={0}/>
        <TextInput formRef={formRef} name={'mapFile'} label={"Merged File Link"} rows={0}/>
        <TextInput formRef={formRef} name={'partitionFile'} label={"Partition File Link"} rows={0}/>
        </Col>
        </Row>
        <RegularButton caption={"Back"} size={undefined} type={"button"} onClick={backList}/>
        <NewButton formRef={formRef} caption={""} size={undefined} type={undefined} onClick={newRecord}/>
        <SaveButton caption={"Save"} size={undefined} type={undefined}/>
        </Form>
        </Col>
        </Row>
        </TabComponent>
        </TabGroup>
        </Col>
        <Col md={4}></Col>
        </Row>
        </Content>
        </div>
    )
}