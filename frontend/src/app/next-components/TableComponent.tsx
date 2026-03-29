'use client'
import checked from '../../components/assets/checked.png';
import Image from 'next/image';
import TableComponentClass from "@/components/Table/TableComponentClass";

export default class TableComponent extends TableComponentClass{

    print(json:any,key:string,indexKey:number,size?:any){
        let value=json[key];
        let id=json[this.idKey];
        let element=<td key={id}  style={{
            //width:size,
            backgroundColor: this.state.selectedRow === indexKey ? "#D3D3D3" : ""
          }} className={ this.state.selectedRow === indexKey ? "TableRowSelect":""}>{value}</td>;
        if(this.valueBoolean(value)){
            element=<td key={id}  style={{
                //width:size,
                backgroundColor: this.state.selectedRow === indexKey ? "#D3D3D3" : ""
              }} className={this.state.selectedRow === indexKey ? "TableRowSelect":""}>
            <Image src={checked} alt="Checked" width={30} height={30}/>
              </td>;
        }
        return element;
        
    }
}