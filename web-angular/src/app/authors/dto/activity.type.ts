export class Activity {
  public id: string;
  public name: string;
  public targetName: string;
  public message: string;
  public content: string;
  public createAt: string;

  constructor(id: string, name: string, targetName: string, message: string, content: string, createAt: string) {
    this.id = id;
    this.name = name;
    this.targetName = targetName;
    this.message = message;
    this.content = content;
    this.createAt = createAt;
  }
}
