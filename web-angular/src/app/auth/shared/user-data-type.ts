export class UserData {
  constructor(
    public username: string,
    public firstName: string,
    public lastName: string,
    public token: string,
    public tokenExpirationDate: number,
  ) {
  }
}
