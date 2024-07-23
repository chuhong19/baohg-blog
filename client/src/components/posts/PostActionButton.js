import Button from 'react-bootstrap/Button'
import likeIcon from '../../assets/like.svg'
import dislikeIcon from '../../assets/dislike.svg'
import likedIcon from '../../assets/liked.svg'
import dislikedIcon from '../../assets/disliked.svg'
import reportIcon from '../../assets/report.svg'
import { PostsContext } from '../../contexts/PostsContext'
import { useContext } from 'react'

const PostActionButton = ({ url, id, onShowReportPostModal, likeStatus, triggerReload }) => {

	const {
        likePost,
        dislikePost,
        unlikePost,
        undislikePost
    } = useContext(PostsContext);
	

	const handleShowReportPostModal = () => {
		onShowReportPostModal();
	};

	const handleLikePost = async () => {
		if (likeStatus === 1) {
			await unlikePost(id);
		} else {
			await likePost(id);
		}
		triggerReload();
	}

	const handleDislikePost = async () => {
		if (likeStatus === -1) {
			await undislikePost(id);
		} else {
			await dislikePost(id);
		}
		triggerReload();
	}

	return (
		<>
			<Button className='post-button' onClick={handleLikePost}>
				<img src={likeStatus === 1 ? likedIcon : likeIcon} alt='like' width='32' height='32' />
			</Button>
			<Button className='post-button' onClick={handleDislikePost}>
				<img src={likeStatus === -1 ? dislikedIcon : dislikeIcon} alt='dislike' width='24' height='24' />
			</Button>
			<Button className='post-button' onClick={handleShowReportPostModal}>
				<img src={reportIcon} alt='report' width='24' height='24' />
			</Button>
		</>
	)
}

export default PostActionButton