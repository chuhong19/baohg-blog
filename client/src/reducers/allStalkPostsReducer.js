import { STALK_POST_LOADED_SUCCESS, STALK_POST_LOADED_FAIL } from "../contexts/constants";

export const allStalkPostsReducer = (state, action) => {
    const { type, payload } = action;
    switch (type) {
        case STALK_POST_LOADED_SUCCESS:
            return {
                ...state,
                allStalkPosts: payload,
                allStalkPostsLoading: false
            };
            

        case STALK_POST_LOADED_FAIL:
            return {
                ...state,
                allStalkPosts: [],
                allStalkPostsLoading: false
            };
        default:
            return state;
    }
}