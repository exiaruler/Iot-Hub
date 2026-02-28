'use client'
import { Component } from "react";
import ColumnBase from "./ColumnBase";
type Props={
    columnName:string;
    size?:any;
    key:string;
}
export default class TableComponentColumn extends ColumnBase{
    
    render(){
        return (
            <th style={{width:this.size}}>
            {this.props.columnName}    
            </th>
        );
    }
}