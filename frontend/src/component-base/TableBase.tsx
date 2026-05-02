import { Component, ReactNode } from "react";
import checked from "../components/assets/checked.png";
import Image from "next/image";
export interface Props{
    id?:string;
    width?:any,
    rowSelect?:boolean;
    children?:Array<ReactNode>;
    results:Array<Record<string,any>>;
    others?:any;
    idKey:string;
    onClick?:CallableFunction;
    onDoubleClick?:CallableFunction;
    unRowSelect?:CallableFunction;
}
interface State{
    selectedRow:number;
    selectRowRec:any;
    rowSelect:boolean;
}

export default class TableBase extends Component<Props,State>{
    width='';
    idKey='';
    selectRowRec=null;
    selectedRow=-1;
    sameRow=false;
    others=this.props.others||'';
    constructor(props:Props) {
            super(props);
            this.state = {
                selectedRow:-1,
                selectRowRec:null,
                rowSelect:false
            };
    }
    componentDidMount(): void {
        if(this.props.rowSelect){
            this.setState({...this.state,rowSelect:this.props.rowSelect})
        }
    }
    valueBoolean(value:any){
        return value === false || value === true;
    }
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
            <Image src={checked.src} alt="Checked" width={30} height={30}/>
              </td>;
        }
        return element;
    }

    selectRow(rec:any,key:number){
        if(this.state.rowSelect){
            if(key==this.state.selectedRow){
                this.setState({...this.state,selectedRow:-1,selectRowRec:null});
                this.selectRowRec=null;
                this.selectedRow=-1;
                this.sameRow=true;
                if(this.props.unRowSelect){
                    this.props.unRowSelect();
                }
            }else {
                this.setState({...this.state,selectedRow:key,selectRowRec:rec});
                this.selectRowRec=rec;
                this.selectedRow=key;
                this.sameRow=false;
            }
            if(this.props.onClick){
                this.props.onClick(rec);
            }
        }
    }
    clearRowSelect(){
        this.setState({...this.state,selectedRow:-1,selectRowRec:null});
        this.selectRowRec=null;
        this.selectedRow=-1;
        this.sameRow=true;
    }
    returnRow(){
        return this.state.selectRowRec;
    }
    returnRowIndex(){
        return this.state.selectedRow;
    }
    doubleClick(){
        if(this.state.rowSelect){
            if(this.props.onDoubleClick){
                this.props.onDoubleClick();
            }
        }
    }
}