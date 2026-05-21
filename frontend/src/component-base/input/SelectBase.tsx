import InputBase from "./InputBase";
import { InputInterface as BaseInterface, State as BaseState } from "../interface/input";
interface InputInterface extends BaseInterface{
    valueKey:string,
    displayKey:string,
    options?:any,
    api?:string,
}
interface State extends BaseState{
    options:any;
    value:Object|null;
    displayValue:string;
    objectValue:Record<string,any>|null;
}
export default class SelectBase extends InputBase{
    declare props:InputInterface;
    declare state:State;
    constructor(props:InputInterface) {
        super(props);
        this.state = {
            options:[],
            value:null,
            displayValue:"",
            objectValue:null,
            warning:''
        };
    }
    componentDidMount(): void {
        if(this.props.value){
            this.setState({...this.state,value:this.props.value});
            this.value=this.props.value;
        }
        this.setOptions();
        this.formHandleValueSet();
    }
    componentDidUpdate(prevProps: Readonly<InputInterface>, prevState: Readonly<State>, snapshot?: any): void {
        this.formHandleValueSetUpdate(prevProps);
        if(this.props.options!=prevState.options){
            const cpy={...this.state};
            cpy.options=this.props.options;
            this.setState(cpy);
        }
    }
    public clearOptions():void{
        const cpy={...this.state};
        cpy.options=[];
        this.setState(cpy);
    }
    public getObjectValue():Record<string,any>|null{
        return this.state.objectValue;
    }
    public setOptions():void{
        if(this.props.options){
            const cpy={...this.state};
            cpy.options=this.props.options;
            this.setState(cpy);
        }else if(this.props.api){

        }
    }
    public setValue(index:number){
        var value=null;
        if(this.props.valueKey){
            value=this.state.options[index][this.props.valueKey];
        }else {
            value=JSON.stringify(this.state.options[index]);
        }
        return value;
    }
    public getObjectValueKey(value:number|string){
        var val=null;
        val=this.props.options.find((rec:any)=>rec[this.props.valueKey]==value);
        return val;
    }
    public onChange(event:any):void{
        const value=event.target.value;
        this.value=event.target.value;
        const statecpy={...this.state};
        if(this.props.valueKey){
            const objectValue=this.props.options.find((ob:any)=>ob[this.props.valueKey]==value)||"";
            statecpy.value=value;
            statecpy.displayValue=objectValue[this.props.displayKey];
            statecpy.objectValue=objectValue;
            this.setState(statecpy);
        }else
        {
            statecpy.value=value;
            statecpy.displayValue=value[this.props.displayKey];
        }
        if(this.props.formRef){
            const form=this.props.formRef.current;
            form.onChangeRecord(this.props.name,value);
        }
        if(this.props.onChange){
            this.props?.onChange(event);
        }
    }
}