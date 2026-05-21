'use client'
import ModalBox from "@/components/modal/ModalBox";
import { Modal, ModalHeader, ModalBody, ModalFooter } from "react-bootstrap";
import Form from "../form/Form";
import SaveButton from "../buttons/SaveButton";
import { RegularButton } from "../buttons/RegularButton";
import { Props } from "@/component-base/form/FormHandle";
import { Ref } from "react";
interface FormModalProps extends Props{
    title:string;
    children?:any;
    buttonChildren?:any;
    submitCaption?:string;
    hideSubmit?:boolean;
    submit?:CallableFunction;
    onClose?:CallableFunction;
    uponUpdate?:CallableFunction;
    formRef:Ref<Form>;
}
interface State{
    show:boolean,
    showSubmit:boolean,
    caption:string
}
export default class FormModal extends ModalBox{
    declare props:FormModalProps;
    formRef:Ref<Form>=null;
    constructor(props:FormModalProps) {
            super(props);
            this.state = {
                show:false,
                showSubmit:false,
                caption:'Save'
            };
    }
   
    componentDidMount(): void {
        if(this.props.submitCaption){
            this.setState({...this.state,caption:this.props.submitCaption});
        }
        if(this.props.hideSubmit){
            this.setState({...this.state,showSubmit:this.props.hideSubmit});
        }
        if(this.props.formRef){
            this.formRef=this.props.formRef;
        }
    }
    componentDidUpdate(prevProps: Readonly<FormModalProps>, prevState: Readonly<State>, snapshot?: any): void {
        if(this.props.uponUpdate){
            this.props.uponUpdate();
        }
    }
    render(){
        return (
        <Modal show={this.state.show} onHide={()=>this.close()}>
        <Form debug={true} ref={this.formRef} onSubmit={this.submitHandle} recordLayout={this.props.recordLayout} idKey={this.props.idKey} record={this.props.record} post={this.props.post} put={this.props.put}>
        <ModalHeader>
        {this.props.title}
        </ModalHeader>
        <ModalBody>
        {
            this.props.children
        }
        </ModalBody>
        <ModalFooter>
        {
            this.props.buttonChildren
        }
        <RegularButton caption={"Close"} size={undefined} type={'button'} onClick={()=>this.close()}/>
        <SaveButton hidden={this.state.showSubmit} caption={this.state.caption} size={undefined} type={'submit'}/>
        </ModalFooter>
        </Form>
        </Modal>
        )
    }
}