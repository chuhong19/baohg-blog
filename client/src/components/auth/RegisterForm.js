import { Button, Form } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { AuthContext } from "../../contexts/AuthContext";
import { useContext, useState } from "react";
import AlertMessage from "../layout/AlertMessage";

const RegisterForm = () => {

  const navigate = useNavigate();

  const { registerUser } = useContext(AuthContext)

  // Local state
  const [registerForm, setRegisterForm] = useState({
    firstname: '',
    lastname: '',
    email: '',
    username: '',
    password: '',
    confirmPassword: '',
    gender: 'MALE'
  });

  const [alert, setAlert] = useState(null);

  const { firstname, lastname, email, username, password, confirmPassword, gender } = registerForm;

  const onChangeRegisterForm = (event) => {
    setRegisterForm({ ...registerForm, [event.target.name]: event.target.value });
  };

  const register = async (event) => {
    event.preventDefault();
    if (password !== confirmPassword) {
      setAlert({ type: 'danger', message: 'Password mismatch' });
      setTimeout(() => setAlert(null), 5000);
      return;
    }
    try {
      const { confirmPassword, ...registerFormDTO } = registerForm;
      const registerData = await registerUser(registerFormDTO);
      if (registerData.data.access_token === null || registerData.data.access_token === undefined) {
        setAlert({ type: 'danger', message: registerData.data });
        setTimeout(() => setAlert(null), 5000);
      } else {
        navigate('/login');
      }
    } catch (error) {
      setAlert({ type: 'danger', message: error });
      setTimeout(() => setAlert(null), 5000);
    }
  }

  return (
    <>
      <Form className='my-4' onSubmit={register}>
        <AlertMessage info={alert} />
        <Form.Group>
          <Form.Control
            type='text'
            placeholder='First name'
            name='firstname'
            required
            value={firstname}
            onChange={onChangeRegisterForm}
          />
        </Form.Group><Form.Group>
          <Form.Control
            type='text'
            placeholder='Last name'
            name='lastname'
            required
            value={lastname}
            onChange={onChangeRegisterForm}
          />
        </Form.Group>
        <Form.Group>
          <Form.Control
            type='text'
            placeholder='Username'
            name='username'
            required
            value={username}
            onChange={onChangeRegisterForm}
          />
        </Form.Group>
        <Form.Group>
          <Form.Control
            type='password'
            placeholder='Password'
            name='password'
            required
            value={password}
            onChange={onChangeRegisterForm}
          />
        </Form.Group>
        <Form.Group>
          <Form.Control
            type='password'
            placeholder='Confirm password'
            name='confirmPassword'
            required
            value={confirmPassword}
            onChange={onChangeRegisterForm}
          />
        </Form.Group>
        <Form.Group>
          <Form.Control
            type='text'
            placeholder='Email'
            name='email'
            required
            value={email}
            onChange={onChangeRegisterForm}
          />
        </Form.Group>
        <Form.Group>
          <Form.Control
            as='select'
            name='gender'
            value={gender}
            onChange={onChangeRegisterForm}
            required
          >
            <option value='MALE'>Male</option>
            <option value='FEMALE'>Female</option>
            <option value='UNKNOWN'>Unknown</option>
          </Form.Control>
        </Form.Group>

        <Button variant='success' type='submit'>
          Register
        </Button>
      </Form>
      <p>
        Already have an account?
        <Link to='/login'>
          <Button variant='info' size='sm' className='ml-2'>
            Login
          </Button>
        </Link>
      </p>
    </>
  );

};

export default RegisterForm;
