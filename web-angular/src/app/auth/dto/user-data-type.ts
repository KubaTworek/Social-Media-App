export class UserData {
  public jwt: string;
  public username: string;
  public expirationTime: Number;

  constructor(jwt: string, username: string) {
    this.jwt = jwt;
    this.username = username;
    this.expirationTime = new Date().getTime() + 10800000;
  }
}
