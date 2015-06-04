//
//  ViewController.m
//  Izi
//
//  Created by Peter on 14/04/15.
//  Copyright (c) 2015 Peter. All rights reserved.
//

#import "RootController.h"
#import "Reachability.h"

@interface RootController ()

@end

@implementation RootController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
    UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(ocultaTeclado:)];
    
    [tapGesture setNumberOfTouchesRequired:1];
    
    [[self view] addGestureRecognizer:tapGesture];
      
}
-(void) ocultaTeclado:(UITapGestureRecognizer *) sender{
    
    //Aqui estao os UITextField da nossa cena
    
    [self.usuarioTextField resignFirstResponder];
    
    [self.senhaTextField resignFirstResponder];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)loginPadraoAction:(id)sender {
    if (self.loginPadraoSwitch.on) {
        NSLog(@"Ativando o login padrao");
        [self criarAlerta:@"Voce ativo o login automatico"];
        
        //desabilitando o textfield para que o usuario nao possa inserir um login
        self.usuarioTextField.enabled = NO;
        self.senhaTextField.enabled = NO;
        
    } else if(!self.loginPadraoSwitch.on){
        NSLog(@"Desabilitando o login autumatico e desabilitando o usuario e senha");
        self.logarAutomaticamenteSwitch.on = NO;
        self.usuarioTextField.enabled = YES;
        self.senhaTextField.enabled = YES;
    } /*else{
        NSLog(@"Inabilitando usuario e senha field");
        self.usuarioTextField.enabled = YES;
        self.senhaTextField.enabled = YES;
    }*/
}
- (IBAction)logarAutomaticamenteAction:(id)sender {
    
    if (self.usuarioTextField.text.length < 5 && self.usuarioTextField.enabled) {
        NSLog(@"O usuario nao digito um login valido");
        [self criarAlerta:@"Precisa usuario e senha OU login padrao"];
        [self.logarAutomaticamenteSwitch setOn:NO];
        
    } else if (self.senhaTextField.enabled==NO && self.usuarioTextField.enabled==NO && self.logarAutomaticamenteSwitch.on) {
        NSLog(@" o usuario desabilito o switch do logar automaticamente");

        [self.logarAutomaticamenteSwitch setOn:YES];

    }
}

-(void)criarAlerta:(NSString *) mensagem{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Alerta"
                                                    message:mensagem
                                                   delegate:self
                                          cancelButtonTitle:@"Aceitar"
                                          otherButtonTitles:nil];
    [alert show];
}

-(BOOL) enviarDadosDeLogin{

    return YES;
}
- (IBAction)testarConexao:(id)sender {
   Reachability *reachability = [Reachability reachabilityForInternetConnection];
    [reachability startNotifier];
    NetworkStatus remoteHostStatus = [reachability currentReachabilityStatus];
        
    if(remoteHostStatus == NotReachable) { NSLog(@"not reachable");
        self.mensagemSucesso.text = @"Nenhuma conexao via Wifi";
    }
    else if (remoteHostStatus == ReachableViaWiFi) {
        NSLog(@"wifi");
        self.mensagemSucesso.text = @"Conectado via Wifi";
    }
    else if (remoteHostStatus == ReachableViaWWAN) { NSLog(@"carrier"); }
 
}
@end
