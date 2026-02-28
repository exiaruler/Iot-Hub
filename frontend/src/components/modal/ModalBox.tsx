'use client'
import { Component, forwardRef, ReactNode, useImperativeHandle, useState } from "react"
import { Modal, ModalBody, ModalFooter, ModalHeader } from "react-bootstrap";
import { ButtonComponent } from "../Buttons/ButtonComponent";
interface modalContent{
    title:string,
    children?:ReactNode,
    buttonChildren?:ReactNode,
    submitCaption?:string,
    hideSubmit?:boolean,
    submit?:CallableFunction,
    onClose?:CallableFunction,
}
interface State{
    show:boolean,
    showSubmit:boolean,
    caption:string
}
// modal box
export default class ModalBox extends Component<modalContent,State>{
    constructor(props:modalContent) {
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
    }
    public open():void{
        this.setState({...this.state,show:true});
    }
    public close=():void=>{
        if(this.props.onClose){
            this.props.onClose();
        }
        this.setState({...this.state,show:false});
    }
    public async submitHandle(event:any){
        if(this.props.submit){
            this.props.submit();
        }
    }
    render(){
        return (
        <Modal show={this.state.show} onHide={()=>this.close()}>
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
        <ButtonComponent caption={"Close"} size={undefined} type={'button'} onClick={()=>this.close()}/>
        {!this.props.hideSubmit?
        <ButtonComponent caption={this.state.caption} size={undefined} type={'button'} onClick={(event:any)=>this.submitHandle(event)}/>
        :null}
        </ModalFooter>
        </Modal>
        )
    }
}