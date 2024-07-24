import { SET_AUTH, UPDATE_USER } from "../contexts/constants";

export const authReducer = (state, action) => {
    const {
        type,
        payload: { isAuthenticated, userId, username, email, firstname, lastname, gender }
    } = action;
    switch (type) {
        case SET_AUTH:
            return {
                ...state,
                authLoading: false,
                isAuthenticated,
                userId,
                username,
                email,
                firstname,
                lastname,
                gender
            }
        case UPDATE_USER:
            return {
                ...state,
                userId,
                username,
                email,
                firstname,
                lastname,
                gender
            }
        default:
            return state;
    }

}