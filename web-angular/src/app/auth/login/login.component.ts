import {Component, OnInit} from '@angular/core';
import {NgForm} from "@angular/forms";
import {DataStorageService} from "../../shared/data-storage.service";
import {LoginRequest} from "../dto/login-request.type";
import {Subscription} from "rxjs";
import {AuthorizationService} from "../service/authorization.service";

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  errorMessage: string = '';
  loginErrorSubscription: Subscription;

  constructor(
    private dataStorageService: DataStorageService,
    private authorizationService: AuthorizationService,
  ) {
    this.loginErrorSubscription = this.authorizationService.getLoginError().subscribe(error => {
      this.errorMessage = error;
    });
  }

  ngOnInit(): void {
    this.errorMessage = '';
  }

  onSubmit(form: NgForm) {
    if (!form.valid) {
      return;
    }

    const username = form.value.username;
    const password = form.value.password;

    const loginRequest: LoginRequest = {
      username: username,
      password: password
    };

    this.dataStorageService.login(loginRequest)

    form.reset();
  }
}
