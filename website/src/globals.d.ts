declare namespace D {
  interface Result<T = any> {
    data?: T;
    code?: number;
    msg?: string;
  }

  interface User {
    username?: string;
    email?: string;
    password?: string;
  }
}
