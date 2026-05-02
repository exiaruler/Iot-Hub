'use client'

import Form from "@/app/next-components/form/Form"
import { ObjectRecord } from "@/app/next-components/layout/Content"
import TabComponent from "@/components/Tab/TabComponent"
import { Col } from "react-bootstrap"
import Row from "react-bootstrap/esm/Row"
interface ModeTabProps{
    modeFormLayout:ObjectRecord;
    boardActLayout:ObjectRecord;
    modeRecord?:ObjectRecord;
    boardActionRecord?:ObjectRecord;
    tabIndex:number;

}
export default function ModeTab(props:ModeTabProps){
    
    return(
        /*
        <TabComponent key={currentModes} title={tabModeDisplay(ref)||newModeLay.mode} eventKey={currentModes}>
            <ModeSubForm recordLayout={newModeLay || {}}
            idKey="id"
            ref={ref}
            objectKey={"mode"}
            index={currentModes}
            formRef={formRef}
            array={true}
            >
            <Row>
            <Col md={4} xs={8}>
            <TextInput
            formRef={ref}
            label="Mode"
            required={true}
            rows={0}
            name="mode"/>
            <SubForm id="BoardAct-Form" recordLayout={boardAct||{}} idKey="id" ref={modeComRef} objectKey={"boardAction"} formRef={ref}>
            {
                compArr
            }
            </SubForm>
            </Col>
            </Row>
            </ModeSubForm>
            <Row>
            <Col xs={16}>
            <RegularButton
            caption="Back"
            type="button"
            onClick={() => tabHandle("command")} size={undefined} />
            <SaveButton caption="Save Function" size={undefined} />
            <RegularButton
            caption="Delete Mode"
            type="button"
            disabled={modesTabFormRef.current.length<1}
            onClick={() => removeMode(currentModes)}
            size={undefined}/>
            <DeleteBox ref={deleteModalRef} title={"Delete"} deleteApi={"/board/"} baseUrl={base.util.baseURL+'/api'} param={board?.id||''}>
            <p>Are you sure you want to delete this function?</p>
            </DeleteBox>
            <RegularButton
            caption="Add Mode"
            type="button"
            onClick={() => addModeForm()} size={undefined}/>
            <NewButton onClick={()=>modeClear(modeComRef)} formRef={ref} caption="Clear Mode" size={undefined} />
            <NewButton formRef={formRef} caption="Clear" size={undefined} />
            </Col>
            </Row>
            </TabComponent>
            */
           <div></div>
    )
            
}