//
//  ViewController.h
//  Izi
//
//  Created by Peter on 14/04/15.
//  Copyright (c) 2015 Peter. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RootController : UIViewController{
}
@property (weak, nonatomic) IBOutlet UITextField *usuarioTextField;
@property (weak, nonatomic) IBOutlet UITextField *senhaTextField;
@property (weak, nonatomic) IBOutlet UISwitch *logarAutomaticamenteSwitch;
- (IBAction)logarAutomaticamenteAction:(id)sender;
@property (weak, nonatomic) IBOutlet UISwitch *loginPadraoSwitch;
- (IBAction)loginPadraoAction:(id)sender;
@property (weak, nonatomic) IBOutlet UILabel *mensagemSucesso;
- (IBAction)testarConexao:(id)sender;

@end

