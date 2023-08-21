export class Author {
  public username: string;
  public firstName: string;
  public lastName: string;

  constructor(
    username: string,
    firstName: string,
    lastName: string
  ) {
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
