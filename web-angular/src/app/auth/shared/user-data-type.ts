export class UserData {
  constructor(
    public username: string,
    public firstName: string,
    public lastName: string,
    public role: string,
    public following: string,
    public followers: string,
    public token: string,
    public tokenExpirationDate: number,
  ) {
  }
}
