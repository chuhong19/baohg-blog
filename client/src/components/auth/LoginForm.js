import { Button, Form } from "react-bootstrap";
import { Link } from "react-router-dom";
import { useState, useContext } from "react";
import { AuthContext } from "../../contexts/AuthContext";
import AlertMessage from "../layout/AlertMessage";

const LoginForm = () => {

    const { loginUser } = useContext(AuthContext)

    const [alert, setAlert] = useState(null);

    const [loginForm, setLoginForm] = useState({
        username: '',
        password: '',
    });
    const { username, password } = loginForm;

    const onChangeLoginForm = (event) =>
        setLoginForm({ ...loginForm, [event.target.name]: event.target.value });

    const login = async (event) => {
        event.preventDefault();
        try {
            const loginData = await loginUser(loginForm);
            if (loginData.data.access_token === null || loginData.data.access_token === undefined) {
                setAlert({ type: 'danger', message: "Invalid username or password" });
                setTimeout(() => setAlert(null), 5000);
            }
        } catch (error) {
            setAlert({ type: 'danger', message: "Server error" });
            setTimeout(() => setAlert(null), 5000);
        }
    }

    return (
        <>
            <Form className='my-4' onSubmit={login}>
                <AlertMessage info={alert} />
                <Form.Group>
                    <Form.Control
                        type='text'
                        placeholder='Username'
                        name='username'
                        required
                        value={username}
                        onChange={onChangeLoginForm}
                    />
                </Form.Group>
                <Form.Group>
                    <Form.Control
                        type='password'
                        placeholder='Password'
                        name='password'
                        required
                        value={password}
                        onChange={onChangeLoginForm}
                    />
                </Form.Group>
                <Button variant='success' type='submit'>
                    Login
                </Button>
            </Form>
            <p>
                Don't have an account?
                <Link to='/register'>
                    <Button variant='info' size='sm' className='ml-2'>
                        Register
                    </Button>
                </Link>
            </p>
        </>
    );
        
};

export default LoginForm;
