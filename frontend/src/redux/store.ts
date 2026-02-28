import { configureStore } from '@reduxjs/toolkit'
import { loginSlice } from './slice/loginSlice';
import { pageSlice } from './slice/pageSlice';
import { appSlice } from './slice/appSlice';
export default configureStore({
    reducer:{
        login:loginSlice.reducer,
        page:pageSlice.reducer,
        app:appSlice.reducer
    }
});