import { Col, Row, Stack, Tab, Table, Tabs } from "react-bootstrap";
import { useParams } from "react-router";
import Form from "../formGen/form";
import { useEffect, useRef, useState } from "react";
import UiBase from "../../base/UiBase";
import { ButtonComponent } from "../../components/Buttons/ButtonComponent";
import React from "react";
import TableComponentColumn from "../../components/Table/TableComponentColumn";
import TabGroup from "../../components/Tab/TabGroup";
import TabComponent from "../../components/Tab/TabComponent";
import Group from "../../components/Group";
import TableComponentClass from "../../components/Table/TableComponentClass";
import FormLayout from "../../components/formGenComponents/layout/formComponent";
type Props={
  entryRecord?:any
}
// page showing records and form
export default function EntryRecord(props:Props){
    const {id}=useParams();
    const formCompRef:any=useRef(null);
    const tableBodyRef:any=useRef(null);
    const [setup,setSetup]=useState({
        name:"",
        form:null,
        getRecordsApi:"",
        deleteApi:"",
        valueKey:"",
        tableColumns:[{}],
        apiKey:""
    });
    var formRef=useRef({});
    var [recordRef,setRecordRef]:any=useState(null);
    var [recordIdRef,setRecordIdRef]=useState("");
    const [records,setRecords]:Array<any>=useState([]);
    const [showFormTab,setShowFormTab]=useState(true);
    const [showDeleteBtn,setShowDeleteBtn]=useState(false);
    const [activeTab, setActiveTab] = useState("records");
    const util=new UiBase();
    
    // get table form
    const getTableForm=async()=>{
      if(props.entryRecord){
        const data=props.entryRecord;
        setShowFormTab(false);
          formRef.current=data.form;
          if(data.deleteApi===""){
            setShowDeleteBtn(true);
          }
          setSetup({
            name:data.name,
            form:data.form,
            getRecordsApi:data.table.getApi,
            deleteApi:data.deleteApi,
            valueKey:data.table.valueKey,
            tableColumns:data.table.tableColumns,
            apiKey:""
          });
          getTableData(data.table.getApi);
      }else
      {
        let url="/entry/get-entry/"+id;
        const request=await util.util.fetchRequest(url,"GET");
        const data=await request.json();
        const status=await request.status;
        if(status===200){
          setShowFormTab(false);
          formRef.current=data.form;
          if(data.deleteApi===""){
            setShowDeleteBtn(true);
          }
          setSetup({
            name:data.name,
            form:data.form,
            getRecordsApi:data.table.getApi,
            deleteApi:data.deleteApi,
            valueKey:data.table.valueKey,
            tableColumns:data.table.tableColumns,
            apiKey:""
          });
          getTableData(data.table.getApi);
        }
      }
    }
    const deleteHandle=async()=>{
      var id="";
      id=recordIdRef;
      if(id!==""){
        const request=await util.util.fetchRequestComplete(setup.deleteApi+id,"DELETE",null,true,false);
        if(request){
          if(request.ok){
            submitHandle();
            formCompRef.current.clearForm();
          }
        }
      }

    }
    const getTableData=async(api:string="")=>{
      if(api==="") api=setup.getRecordsApi;
      if(records.length>0)setRecords([{}]);
      const request=await util.util.fetchRequest(api,"GET");
      const data=await request.json();
      setRecords(data);
    }
    const handleTabSwitch = (tabKey:any) => {
      setActiveTab(tabKey);
    };
    const selectRecord=(rec:any)=>{
      if(rec!=null){
        setRecordRef(rec);
        let idValue=rec[setup.valueKey];
        setRecordIdRef(idValue);
        let form=formCompRef.current;
        form.clearError();
      }
    }
    const unSelectRecord=()=>{
      clearHandle();
      formCompRef.current.clearError();
    }
    const addRecord=()=>{
      setRecordRef(null);
      setRecordIdRef("0");
      handleTabSwitch("record");
      tableBodyRef.current.selectRow(null,-1);
      formCompRef.current.clearForm();
    }
    const submitHandle=()=>{
      handleTabSwitch("records");
      clearHandle();
      getTableData();
    }
    const clearHandle=(refresh:boolean=false)=>{
      setRecordRef(null);
      tableBodyRef.current.selectRow(null,-1);
      setRecordIdRef("0");
      if(refresh){
        getTableData();
      }
    }
    
    useEffect(() => {
      getTableForm();
      
    },[]);
    return(
        <div>
        <Group>
        <Row>
        <Col md={2}>
     
        </Col>
        <Col md={9}>
        <TabGroup defaultActiveKey={"records"}>
        </TabGroup>
        <Tabs 
        id="TabBar"
        activeKey={activeTab}
        onSelect={(tabKey:any) => setActiveTab(tabKey)}
        defaultActiveKey="records"
        className="mb-4">
        <Tab eventKey="records" title={setup.name}>
       
        <TableComponentClass id="table" ref={tableBodyRef} results={records} idKey={setup.valueKey} rowSelect={true} onClick={selectRecord} onDoubleClick={()=>handleTabSwitch("record")}>
        {
          setup.tableColumns.map((col:any,index)=>(
            <TableComponentColumn key={col.key} columnName={col.name}/>
          ))
        }
        </TableComponentClass>
        <Stack direction="horizontal" gap={1}>
        <ButtonComponent id={""} caption={'Add'} variant={''} onClick={addRecord} size={''} active={false} disabled={false} type={undefined} />
        {!showDeleteBtn?
        <ButtonComponent id={""} caption={'Delete'} variant={'danger'} onClick={deleteHandle} size={''} active={false} disabled={false} type={undefined} />
        :null}
        </Stack>
        </Tab>
        {!showFormTab?
        <Tab eventKey="record" title={setup.name+" Record"}>
        {
        <FormLayout ref={formCompRef} form={setup.form} entry={true} valueKey={setup.valueKey} id={recordIdRef} record={recordRef} onClick={() => handleTabSwitch("records")} submitHandle={submitHandle} clearHandle={clearHandle} formId={""}/>
        }
        </Tab>
        :null}
        </Tabs>
        </Col>
        <Col md={4}>
        </Col>
        </Row>
        </Group>
        </div>
    );
}
