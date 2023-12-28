import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {BehaviorSubject, Observable} from "rxjs";
import {UserData} from "../shared/user-data-type";

@Injectable({
  providedIn: 'root',
})
export class AuthorizationService {
  private loginErrorSubject = new BehaviorSubject<string>('');
  private registerErrorSubject = new BehaviorSubject<string>('');
  private userSubject = new BehaviorSubject<UserData | null>(this.getUserData());

  constructor(
    private router: Router,
  ) {
    this.setupInteractionsListener();
  }

  getLoginError(): Observable<string> {
    return this.loginErrorSubject.asObservable();
  }

  getRegisterError(): Observable<string> {
    return this.registerErrorSubject.asObservable();
  }

  handleLogin(userData: UserData) {
    console.log(userData.following);
    if (userData) {
      this.storeUserData(userData);
      this.userSubject.next(userData); // Emit the user value
      this.router.navigate(['/home']);
    }
  }

  getUser(): Observable<UserData | null> {
    return this.userSubject.asObservable();
  }

  handleRegister() {
    this.router.navigate(['auth/login']);
  }

  logout() {
    this.removeUserData();
    this.router.navigate(['auth/login']);
  }

  handleLoginError(errorMessage: string) {
    this.loginErrorSubject.next(errorMessage);
  }

  handleRegisterError(errorMessage: string) {
    this.registerErrorSubject.next(errorMessage);
  }

  getToken(): string | null {
    const userDataJson = this.getUserData();
    return userDataJson ? String(userDataJson.token) : null;
  }

  getUsername(): string | null {
    const userDataJson = this.getUserData();
    return userDataJson ? String(userDataJson.username) : null;
  }

  getRole(): string | null {
    const userDataJson = this.getUserData();
    return userDataJson ? String(userDataJson.role) : null;
  }

  private setupInteractionsListener() {
    document.addEventListener("mousemove", () => {
      if (this.isSessionExpired()) {
        this.logout();
      }
    });
  }

  private getUserData(): UserData {
    const userDataJson = sessionStorage.getItem("userData");
    return userDataJson ? JSON.parse(userDataJson) : null;
  }

  private storeUserData(userData: UserData) {
    sessionStorage.setItem('userData', JSON.stringify(userData));
  }

  private removeUserData() {
    sessionStorage.removeItem('userData');
  }

  private isSessionExpired() {
    const userDataJson = this.getUserData();
    const logoutTime = userDataJson ? Number(userDataJson.tokenExpirationDate) : null;

    return logoutTime !== null && logoutTime <= new Date().getTime();
  }
}
