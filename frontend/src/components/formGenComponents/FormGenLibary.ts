import Util from '../../base/Util';
import { FormGenText } from './FormGenText';
import { FormGenTextArea } from './FormGenTextArea';
import { FormGenCheckBox } from './FormGenCheckBox';
import {FormAPI} from '../../api/FormAPI';
import PasswordText from '../PasswordText';
import RgbHexSelect from './RgbHexSelect';
export default class FormGenLibary extends Util{
    private api=new FormAPI();
    
    public components=[
        {
            componentName:"text",
            component:FormGenText,
            status:true
        },
        {
            componentName:"textarea",
            component:FormGenTextArea,
            status:true
        },
        {
            componentName:"password",
            component:PasswordText,
            status:true
        },
        {
            componentName:"checkbox",
            component:FormGenCheckBox,
            status:true
        },
        {
            componentName:"rgbhexselect",
            component:RgbHexSelect,
            status:true
        }
    ].filter((comp)=>comp.status===true);
    // Testing and sample
    public forms=[
        {
            param:"sample",
            name:"Sample Form",
            status:false,
            postApi:"",
            updateApi:"",
            headers:this.getApiKey(),
            redirect: "",
            retrieveApi: "",
            // protected
            authorisedAccess: false,
            // useState model
            stateModel:[
                {
                    "key": "name",
                    "value": ""
                },
                {
                    "key": "lastName",
                    "value": ""
                }
            ],
            // form 
            form:[
                    {
                        component:"text",
                        label:"name",
                        type:"string",
                        disabled:false,
                        required:true,
                        name:"name"
                    },
                    {
                        component:"text",
                        label:"last name",
                        type:"string",
                        disabled:false,
                        required:true,
                        name:"lastName"
                    }
                ]
        }
    ].filter((form)=>form.status===true);

    public findInhouseForm(formParam:any){
        let form=null;
        if(this.forms.find((form)=>form.param===formParam)!==undefined){
            form=this.forms.find((form)=>form.param===formParam);
        }
        return form;
    }
    public findComponent(componentName:String){
        let comp=null;
        if(this.components.find((form)=>form.componentName===componentName)!==undefined){
            comp=this.components.find((form)=>form.componentName===componentName);
        }
        return comp?.component;
    }
}