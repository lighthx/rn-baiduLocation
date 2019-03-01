export interface Location{
    latitude:number;
    longitude:number;
    country:string;
    city:string;
    province:string;
    district:string;
    street:string;
    streetNumber:string;
    cityCode:string;
}
export interface CheckProps {
    gpsIsOpen:boolean;
    gpsPermissionIsOpen:boolean;
}
export const init:()=>Promise<void>;
export const getLocation:()=>Promise<Location>;
export const toGpsSetting:()=>Promise<void>;
export const toAppSetting:()=>Promise<void>;
export const checkPermission:()=>Promise<CheckProps>;