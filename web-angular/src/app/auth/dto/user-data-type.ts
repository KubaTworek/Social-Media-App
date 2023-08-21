export class UserData {
  public username: string;
  public firstName: string;
  public lastName: string;
  public token: string;
  public tokenExpirationDate: Number;

  constructor(username: string, firstName: string, lastName: string, token: string, tokenExpirationDate: Number) {
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.token = token;
    this.tokenExpirationDate = tokenExpirationDate
  }
}
