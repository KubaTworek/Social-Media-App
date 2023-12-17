export class RegisterRequest {
  constructor(
    public username: string,
    public password: string,
    public firstName: string,
    public lastName: string,
    public role: string,
  ) {
  }
}
