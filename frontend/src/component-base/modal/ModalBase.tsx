'use client'

import { Component, ReactNode } from "react";
export interface ModalContent{
    title:string;
    children?:ReactNode;
    buttonChildren?:ReactNode;
    submitCaption?:string|'Submit';
    closeCaption?:string|'Close';
    showSubmit?:boolean;
    submit?:CallableFunction;
    onClose?:CallableFunction;
}
export interface State{
    show:boolean;
    showSubmit:boolean;
}
export default class ModalBase extends Component<ModalContent, State> {
    constructor(props:ModalContent) {
            super(props);
            this.state = {
                show:false,
                showSubmit:false
            };
    }
    componentDidMount(): void {
        if(this.props.showSubmit){
            this.setState({...this.state,showSubmit:this.props.showSubmit});
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
    public async submitHandle(event:unknown){
        if(this.props.submit){
            this.props.submit();
        }
    }
}