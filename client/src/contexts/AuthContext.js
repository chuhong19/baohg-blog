import { createContext, useEffect, useReducer, useState } from "react";
import { authReducer } from "../reducers/authReducer";
import { apiUrl, LOCAL_STORAGE_TOKEN_NAME, SET_AUTH, UPDATE_USER } from "./constants";
import axios from "axios";
import setAuthToken from "../utils/setAuthToken";
import { useNavigate } from "react-router-dom";

export const AuthContext = createContext();

const AuthContextProvider = ({ children }) => {

    const [authState, dispatch] = useReducer(authReducer, {
        authLoading: true,
        isAuthenticated: false,
        userId: null,
        username: null,
        email: null,
        firstname: null,
        lastname: null,
        gender: null
    })

    const loadUser = async () => {
        if (localStorage[LOCAL_STORAGE_TOKEN_NAME]) {
            setAuthToken(localStorage[LOCAL_STORAGE_TOKEN_NAME]);
        }
        try {
            const response = await axios.post(`${apiUrl}/user/getProfile`);
            if (response.data.data.username !== "") {
                dispatch({
                    type: SET_AUTH,
                    payload: {
                        isAuthenticated: true,
                        userId: response.data.data.userId,
                        username: response.data.data.username,
                        email: response.data.data.email,
                        firstname: response.data.data.firstname,
                        lastname: response.data.data.lastname,
                        gender: response.data.data.gender
                    }
                })
            }
            else if (response.data.data.username === "") {
                dispatch({
                    type: SET_AUTH,
                    payload: {
                        isAuthenticated: false,
                        userId: null,
                        username: null,
                        email: null,
                        firstname: null,
                        lastname: null,
                        gender: null
                    }
                })
            }
        } catch (err) {
            localStorage.removeItem(LOCAL_STORAGE_TOKEN_NAME);
            setAuthToken(null);
            dispatch({
                type: SET_AUTH,
                payload: {
                    isAuthenticated: false,
                    userId: null,
                    username: null,
                    email: null,
                    firstname: null,
                    lastname: null,
                    gender: null
                }
            })
        }
    }

    const updateUser = async (updatedUser) => {
        try {
            const response = await axios.post(`${apiUrl}/user/update`, {
                userId: updatedUser.userId,
                firstname: updatedUser.firstname,
                lastname: updatedUser.lastname,
                gender: updatedUser.gender
            });
            if (response.data.code === '200') {
                const payload = response.data.data;
                dispatch({ type: UPDATE_USER, payload: payload });
            } else {

            }
        } catch (error) {
            return error;
        }
    }

    useEffect(() => {
        loadUser()
    }, []);

    const loginUser = async loginForm => {
        try {
            const response = await axios.post(`${apiUrl}/auth/authenticate`, loginForm);
            const access_token = response.data.data.access_token;
            if (access_token !== null)
                localStorage.setItem(LOCAL_STORAGE_TOKEN_NAME, access_token);
            await loadUser();
            return response.data;
        } catch (error) {
            if (error.response.data) return error.response.data;
            else return {
                success: false,
                message: error.message
            }
        }
    }

    const registerUser = async registerForm => {
        try {
            const response = await axios.post(`${apiUrl}/auth/register`, registerForm);
            // const { success, message } = response.data;
            return response.data;
        } catch (error) {
            if (error.response.data) return error.response.data;
            else return {
                success: false,
                message: error.message
            }
        }
    }

    const navigate = useNavigate();

    // Logout
    const logoutUser = () => {
        localStorage.removeItem(LOCAL_STORAGE_TOKEN_NAME);
        dispatch({
            type: 'SET_AUTH',
            payload: {
                isAuthenticated: false,
                userId: null,
                username: null,
                email: null,
                firstname: null,
                lastname: null,
                gender: null
            },
        });
        navigate('/login');
    };

    // Context data
    const authContextData = {
        authState,
        loadUser,
        updateUser,
        loginUser,
        registerUser,
        logoutUser
    };

    return (
        <AuthContext.Provider value={authContextData}>
            {children}
        </AuthContext.Provider>
    )
}

export default AuthContextProvider;