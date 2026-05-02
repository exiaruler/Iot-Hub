'use client'
import checked from '../../components/assets/checked.png';
import Image from 'next/image';
import TableComponentClass from "@/components/Table/TableComponentClass";
import { Props as TableProps } from '@/component-base/TableBase';
import { ObjectArray, ObjectRecord } from '@/NextBase';
import { Table } from 'react-bootstrap';

export default class TableComponent extends TableComponentClass{
    //declare props:Props;
    print(json:any,key:string,indexKey:number,size?:any,functionDisplay?:CallableFunction){
        let value=json[key];
        let id=json[this.idKey];
        if(functionDisplay&&key!=""){
            const displayCall=functionDisplay(value);
            value=displayCall;
        }
        let element=<td key={id}  style={{
            //width:size,
            backgroundColor: this.state.selectedRow === indexKey ? "#D3D3D3" : ""
          }} className={ this.state.selectedRow === indexKey ? "TableRowSelect":""}>{value}</td>;
        if(this.valueBoolean(value)){
            let img:any='';
            if(value) img=<Image src={checked} alt="Checked" width={30} height={30}/>;
            element=<td key={id}  style={{
                //width:size,
                backgroundColor: this.state.selectedRow === indexKey ? "#D3D3D3" : ""
              }} className={this.state.selectedRow === indexKey ? "TableRowSelect":""}>
            {img}
            </td>;
        }
        return element;
        
    }
    render(){
        return (
        <Table hover={this.state.rowSelect} bordered  {...this.others} width={this.width} responsive={"md"} size={"sm"} id={this.props.id} >
        <thead>
        <tr>
        {
            this.props.children
        }
        </tr>
        </thead>
        <tbody>
        {
            this.props.results.map((rec:ObjectRecord,index)=>(
                <tr onClick={()=>this.selectRow(rec,index)} onDoubleClick={()=>this.doubleClick()}>
                {
                    this.props.children?.map((ele:any)=>(
                        this.print(rec,ele.key,index,ele.props.size,ele.props.functionDisplay)
                    ))
                }
                </tr>
            ))
        }
        {
        this.props.results.length===0?
            <tr>
                <td colSpan={this.props.children?.length} style={{textAlign:'center'}}>No results avaliable</td>
            </tr>
        :null
        }
        </tbody>
        </Table>
        );
    }
}