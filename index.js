import {NativeModules, Platform, DeviceEventEmitter} from 'react-native';

const {RNBaiduLocation} = NativeModules;
const isIos = Platform.OS === "ios"
export const init = async () => {
    await RNBaiduLocation.init();
}

export const getLocation = async () => {

    const data = await RNBaiduLocation.getLocation()
    if (isIos) {
        const promise = new Promise((resolve, reject) => {
            if (!!data.latitutde) {
                resolve(data)
            } else {
                reject(data.errorMessage)
            }
        })
        return await promise;

    } else {
        const promise = new Promise((resolve, reject) => {
            DeviceEventEmitter.addListener("locationData", (e) => {
                resolve(e)
            })
        })
        return await promise;

    }
}

export const toAppSetting = async () => {
        await RNBaiduLocation.toAppSetting()
}

export const toGpsSetting=async()=>{
    if(!isIos){
        await RNBaiduLocation.toGpsSetting()
    }

}

export const checkPermission = async () => {
        const data = await RNBaiduLocation.check()
        return data

}

