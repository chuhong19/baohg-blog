import { MY_POST_LOADED_SUCCESS, MY_POST_LOADED_FAIL, UPDATE_POST, FIND_POST, DELETE_POST, ADD_POST } from "../contexts/constants";

export const allMyPostsReducer = (state, action) => {
    const { type, payload } = action;
    switch (type) {
        case MY_POST_LOADED_SUCCESS:
            return {
                ...state,
                allMyPosts: payload,
                allMyPostsLoading: false
            };

        case MY_POST_LOADED_FAIL:
            return {
                ...state,
                allMyPosts: [],
                allMyPostsLoading: false
            };
        case ADD_POST:
            return {
                ...state,
                allMyPosts: [...state.allMyPosts, payload]
            };
        case FIND_POST:
            return {
                ...state,
                activePost: payload,
            };

        case UPDATE_POST:
            const newPosts = state.allMyPosts.map((post) =>
                post.id === payload.id ? payload : post
            );
            return {
                ...state,
                allMyPosts: newPosts,
            };
        case DELETE_POST:
            const remainPosts = state.allMyPosts.filter((post) => post._id !== payload)
            return {
                ...state,
                allMyPosts: remainPosts,
            };
        default:
            return state;
    }
}