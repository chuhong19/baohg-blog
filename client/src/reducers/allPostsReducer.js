import { ALL_POST_LOADED_SUCCESS, ALL_POST_LOADED_FAIL } from "../contexts/constants";

export const allPostsReducer = (state, action) => {
    const { type, payload } = action;
    switch (type) {
        case ALL_POST_LOADED_SUCCESS:
            return {
                ...state,
                allPosts: payload,
                allPostsLoading: false,
            };

        case ALL_POST_LOADED_FAIL:
            return {
                ...state,
                allPosts: [],
                allPostsLoading: false,
            };
            
        default:
            return state;
    }
}