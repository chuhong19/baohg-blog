import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import blogLogo from '../../assets/logo.svg';
import logoutIcon from '../../assets/logout.svg';
import Button from 'react-bootstrap/Button';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../contexts/AuthContext';
import { useContext } from 'react';

const NavbarMenu = ({ children }) => {

  const {
    authState: {
      username,
    },
    logoutUser,
  } = useContext(AuthContext);

  const logout = () => logoutUser();

  return (
    <>
      <Navbar expand='lg' bg='primary' variant='dark' className='shadow'>
        <Navbar.Brand className='font-weight-bolder text-white'>
          <img
            src={blogLogo}
            alt='blogLogo'
            width='32'
            height='32'
            className='mr-2'
          />
          CBH
        </Navbar.Brand>
        <Navbar.Toggle aria-controls='basic-navbar-nav' />
        <Navbar.Collapse id='basic-navbar-nav'>
          <Nav className='mr-auto'>
          <Nav.Link
              className='font-weight-bolder text-white'
              to='/dashboard'
              as={Link}
            >
              Blog
            </Nav.Link>
            <Nav.Link
              className='font-weight-bolder text-white'
              to='/home'
              as={Link}
            >
              Home
            </Nav.Link>
            <Nav.Link
              className='font-weight-bolder text-white'
              to='/community'
              as={Link}
            >
              Community
            </Nav.Link>
            <Nav.Link
              className='font-weight-bolder text-white'
              to='/tictactoe'
              as={Link}
            >
              Tictactoe
            </Nav.Link>
            <Nav.Link
              className='font-weight-bolder text-white'
              to='/about'
              as={Link}
            >
              About
            </Nav.Link>
            <Nav.Link
              className='font-weight-bolder text-white'
              to='/profile'
              as={Link}
            >
              Profile
            </Nav.Link>
          </Nav>

          <Nav>
            <Nav.Link className='font-weight-bolder text-white' disabled>
              Welcome {username}
            </Nav.Link>
            <Button
              variant='secondary'
              className='font-weight-bolder text-white'
              onClick={logout}
            >
              <img
                src={logoutIcon}
                alt='logoutIcon'
                width='32'
                height='32'
                className='mr-2'
              />
              Logout
            </Button>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
      {children}
    </>
  );
};

export default NavbarMenu;
