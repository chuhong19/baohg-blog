export const apiUrl = process.env.NODE_ENV !== 'production' ? 'http://localhost:8080' : 'sth';

export const LOCAL_STORAGE_TOKEN_NAME = 'access-token';

export const SET_AUTH = 'SET_AUTH';
export const UPDATE_USER = 'UPDATE_USER';

export const ALL_POST_LOADED_SUCCESS = 'ALL_POST_LOADED_SUCCESS';
export const ALL_POST_LOADED_FAIL = 'ALL_POST_LOADED_FAIL';

export const MY_POST_LOADED_SUCCESS = 'MY_POST_LOADED_SUCCESS';
export const MY_POST_LOADED_FAIL = 'MY_POST_LOADED_FAIL';

export const STALK_POST_LOADED_SUCCESS = 'STALK_POST_LOADED_SUCCESS';
export const STALK_POST_LOADED_FAIL = 'STALK_POST_LOADED_FAIL';

export const ADD_POST = 'ADD_POST';

export const UPDATE_POST = 'UPDATE_POST';
export const FIND_POST = 'FIND_POST';

export const RELOAD_POST = 'RELOAD_POST';
export const DELETE_POST = 'DELETE_POST';
