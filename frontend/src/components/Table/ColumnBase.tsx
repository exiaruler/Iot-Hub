'use client'
import { Component } from "react";
interface Props{
    columnName:string;
    size?:any;
    key:string;
}

export default class ColumnBase extends Component<Props>{
    public key=this.props.key;
    public size='';
    constructor(props:Props) {
        super(props);
        this.state = {
            
        };
       
        if(this.props.size){
            this.size=this.props.size;
        }
    }
    componentDidMount(): void {
        
    }
}